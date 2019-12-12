package com.totalcross.agralapp.gfx;

import java.io.IOException;
import java.util.Vector;

import com.totalcross.agralapp.acquisition.Acquisitor;
import com.totalcross.agralapp.acquisition.ReadCSV;
import com.totalcross.agralapp.view.MathUtils;

import totalcross.ui.ClippedContainer;
import totalcross.ui.MainWindow;
import totalcross.ui.event.UpdateListener;
import totalcross.ui.gfx.Coord;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.media.Camera;

public class CameraContainer extends ClippedContainer {
    
    private int lineColor;
    Acquisitor points;
    int index = 1;
    int millisecondsToUpdate = 400;
    int elapsedMilliseconds = 0;
    Coord[] oldSquare = null;
    int scale = 30;
    Vector<int[][]> polygonsToDraw = new Vector<>(20);
    UpdateListener updateListener = new UpdateListener(){
        
        @Override
        public void updateListenerTriggered(int elapsedMilliseconds) {
            CameraContainer.this.elapsedMilliseconds += elapsedMilliseconds;
            if (CameraContainer.this.elapsedMilliseconds >= millisecondsToUpdate) {
                CameraContainer.this.elapsedMilliseconds = 0;
                polygonsToDraw.add(getPolygonToDraw(index));
                repaintNow();
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
        
        int oldColor = g.backColor;
        g.backColor = this.lineColor;
        for (int[][] polygon: polygonsToDraw) {
            g.fillPolygon(polygon[0], polygon[1], polygon[0].length);
        }
        g.backColor = oldColor;
        for (int i = 1; i < this.points.size(); i++) {
            g.drawLine(Math.round(this.points.getX(i - 1).floatValue()*scale), Math.round(this.points.getY(i - 1).floatValue()*scale)
                        , Math.round(this.points.getX(i).floatValue()*scale), Math.round(this.points.getY(i).floatValue()*scale));
        }
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    private int[][] getPolygonToDraw(int index) {
        int[][] coord;
        Coord[] square = MathUtils.getSquarePoints(new Coord(Math.round(points.getX(index - 1).floatValue()*scale), Math.round(points.getY(index - 1).floatValue()*scale))
                                , new Coord(Math.round(points.getX(index).floatValue()*scale), Math.round(points.getY(index).floatValue()*scale))
                                , 20);
        if (oldSquare != null) {
            if(MathUtils.isInsideSquare(square[0], oldSquare)) {
                coord = new int[][] {new int[] {oldSquare[1].x, oldSquare[3].x, square[1].x, square[3].x, square[2].x, oldSquare[2].x, oldSquare[0].x}
                , new int[] {oldSquare[1].y, oldSquare[3].y, square[1].y, square[3].y, square[2].y, oldSquare[2].y, oldSquare[0].y}};
            } else if (MathUtils.isInsideSquare(square[1], oldSquare)) {
                coord = new int[][] {new int[] {oldSquare[1].x, oldSquare[3].x, square[3].x, square[2].x, square[1].x, oldSquare[2].x, oldSquare[0].x}
                , new int[] {oldSquare[1].y, oldSquare[3].y, square[3].y, square[2].y, square[1].y, oldSquare[2].y, oldSquare[0].y}};
            } else {
                coord = new int[][] {new int[] {square[0].x, square[1].x, square[2].x, square[3].x}
                , new int[] {square[0].y, square[1].y, square[2].y, square[3].y}};
            }
        } else {
            coord = new int[][] {new int[] {square[0].x, square[1].x, square[2].x, square[3].x}
            , new int[] {square[0].y, square[1].y, square[2].y, square[3].y}};
        }
        oldSquare = square;
        return coord;
    }
}

