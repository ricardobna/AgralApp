package com.totalcross.agralapp.gfx;

import java.io.IOException;
import java.util.Vector;

import com.totalcross.agralapp.acquisition.Acquisitor;
import com.totalcross.agralapp.acquisition.ReadCSV;
import com.totalcross.agralapp.view.MathUtils;

import totalcross.ui.ClippedContainer;
import totalcross.ui.MainWindow;
import totalcross.ui.Window;
import totalcross.ui.event.UpdateListener;
import totalcross.ui.gfx.Coord;
import totalcross.ui.gfx.Graphics;

public class CameraContainer extends ClippedContainer {
    
    private int lineColor;
    Acquisitor points;
    int index = 1;
    int millisecondsToUpdate = 150;
    int elapsedMilliseconds = 0;
    Coord[] oldSquare = null;
    int scale = 300;
    int bladeWidth = 160;
    int xVariation;
    int yVariation;
    int lastPolygonX0;
    int lastPolygonY0;
    int lastPolygonX1;
    int lastPolygonY1;
    Vector<int[][]> polygonsToDraw = new Vector<>(20);
    UpdateListener updateListener = new UpdateListener(){
        
        @Override
        public void updateListenerTriggered(int elapsedMilliseconds) {
            CameraContainer.this.elapsedMilliseconds += elapsedMilliseconds;
            if (CameraContainer.this.elapsedMilliseconds >= millisecondsToUpdate) {
                CameraContainer.this.elapsedMilliseconds = 0;
                polygonsToDraw.add(getPolygonToDraw(index));
                xVariation = CameraContainer.this.getWidth()/2 - Math.round(CameraContainer.this.points.getX(index).floatValue() * scale);
                yVariation = CameraContainer.this.getHeight()/2 - Math.round(CameraContainer.this.points.getY(index).floatValue() * scale);
                
                Window.repaintActiveWindows();
                index++;
                if (index > points.size() - 1) {
                    MainWindow.getMainWindow().removeUpdateListener(updateListener);
                }
            }
        }
    };

    public CameraContainer() {
        try {
            this.points = ReadCSV.read("resources/sample.csv");
            MainWindow.getMainWindow().addUpdateListener(updateListener);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onPaint(Graphics g) {
        //Draws the line to try and focus
        g.fillRect(0, 0, getWidth(), getHeight());
        int oldColor = g.backColor;
        g.backColor = this.lineColor;
        for (int[][] polygon : polygonsToDraw) {
            fillTranslatedPolygon(xVariation, yVariation, g, polygon);
        }
        g.backColor = oldColor;
        g.drawThickLine(lastPolygonX0 + xVariation, lastPolygonY0 + yVariation, lastPolygonX1 + xVariation, lastPolygonY1 + yVariation, 2);
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    private int[][] getPolygonToDraw(int index) {
        int[][] coord;
        
        Coord[] square = MathUtils.getSquarePoints(new Coord(Math.round(points.getX(index - 1).floatValue()*scale), Math.round(points.getY(index - 1).floatValue()*scale))
                                , new Coord(Math.round(points.getX(index).floatValue()*scale), Math.round(points.getY(index).floatValue()*scale))
                                , bladeWidth);
        if (oldSquare != null) {
            if(MathUtils.isInsideSquare(square[0], oldSquare)) {
                coord = new int[][] {
                        new int[] {oldSquare[0].x, oldSquare[3].x, square[3].x, square[2].x, square[1].x, oldSquare[2].x, oldSquare[1].x}, 
                        new int[] {oldSquare[0].y, oldSquare[3].y, square[3].y, square[2].y, square[1].y, oldSquare[2].y, oldSquare[1].y}};
            } else {
                coord = new int[][] {
                        new int[] {oldSquare[0].x, oldSquare[3].x, square[0].x, square[3].x, square[2].x, oldSquare[2].x, oldSquare[1].x}, 
                        new int[] {oldSquare[0].y, oldSquare[3].y, square[0].y, square[3].y, square[2].y, oldSquare[2].y, oldSquare[1].y}};
            }
        } else {
            coord = new int[][] {
                    new int[] {square[0].x, square[3].x, square[2].x, square[1].x}, 
                    new int[] {square[0].y, square[3].y, square[2].y, square[1].y}};
        }
        lastPolygonX0 = square[3].x;
        lastPolygonY0 = square[3].y;
        lastPolygonY1 = square[2].y;
        lastPolygonX1 = square[2].x;

        oldSquare = square;
        return coord;
    }
    
    private void fillTranslatedPolygon(int xVariation, int yVariation, Graphics g, int[][] polygon) {
        int[] xPolygon = new int[polygon[0].length];
        int[] yPolygon = new int[polygon[1].length];
        for(int i = 0; i < polygon[0].length; i++) {
            xPolygon[i] = polygon[0][i] + xVariation;
            yPolygon[i] = polygon[1][i] + yVariation;
        }
        g.fillPolygon(xPolygon, yPolygon, xPolygon.length);
    }

}
