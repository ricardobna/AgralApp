package com.totalcross.agralapp.gfx;

import java.io.IOException;
import java.util.Vector;

import com.totalcross.agralapp.acquisition.Acquisitor;
import com.totalcross.agralapp.acquisition.ReadCSV;

import totalcross.ui.ClippedContainer;
import totalcross.ui.gfx.Coord;
import totalcross.ui.gfx.Graphics;
import totalcross.util.UnitsConverter;

public class CameraContainer extends ClippedContainer {

    private Vector<Integer> polygonVerticesX = new Vector<>(20);
    private Vector<Integer> polygonVerticesY = new Vector<>(20);
    private int vehicleX;
    private int vehicleY;
    private int lineColor;
    Acquisitor points;

    public CameraContainer() {
        try {
            this.points = ReadCSV.read("C:\\Users\\Ricardo-LAPTOP\\Documents\\TotalCross\\Projects\\AgralApp\\src\\main\\resources\\sample.csv");
            this.vehicleX = Math.round(this.points.originX().floatValue());
            this.vehicleY = Math.round(this.points.originY().floatValue());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaint(Graphics g) {
        //Draws the line to try and focus
        int scale = 10;
        for (int i = 1; i < this.points.size(); i++) {
            drawSection(new Coord(this.points.getX(i - 1).intValue() * scale, this.points.getY(i - 1).intValue() * scale)
            , new Coord(this.points.getX(i).intValue() * scale, this.points.getY(i).intValue() * scale), 20, i == 1);
        }
        // for(int i = 0; i < this.points.size(); i++) {
        //     updatePolygons(Math.round(this.points.getX(i).floatValue())
        //                  , Math.round(this.points.getY(i).floatValue()));
        // }
        System.out.println();
        int oldColor = g.backColor;
        g.backColor = this.lineColor;
        for(int i = 3; i < this.polygonVerticesX.size(); i += 2) {
            System.out.println(getVertices(this.polygonVerticesX, i).toString());
            g.fillPolygon(getVertices(this.polygonVerticesX, i), getVertices(this.polygonVerticesY, i), 4);
        }
        g.backColor = oldColor;
    }

    private int[] getVertices(Vector<Integer> vertices, int index) {
        int[] vertexes = new int[4];
        for(int i = 0; i < 4; i++) {
            vertexes[i] = vertices.get(index - i);
        }
        return vertexes;
    }

    private void updatePolygons(int newX, int newY) {
        //Translocates the vehicle position to 0, and making the new point translocate the same amount
        int transformedNewX = newX - this.vehicleX;
        int transformedNewY = newY - this.vehicleY;
        //Rotating the point by m rad and scaling
        double m = Math.acos(((double)newY - this.vehicleY)/(newX - this.vehicleX));
        transformedNewX = Math.round((float)((transformedNewX * Math.cos(m) - transformedNewY * Math.sin(m))));
        transformedNewY = Math.round((float)((transformedNewX * Math.sin(m) + transformedNewY * Math.cos(m))));

        //Now we rotate back and translocate it to its original position;
        int firstXToAdd = transformedNewY;
        int firstYToAdd = 0;
        firstXToAdd = Math.round((float)(firstXToAdd * Math.cos(-m) - firstYToAdd * Math.sin(-m))) + this.vehicleX;
        firstYToAdd = Math.round((float)(firstXToAdd * Math.sin(-m) + firstXToAdd * Math.cos(-m))) + this.vehicleY;
        //Same for the second point
        int secondXToAdd = transformedNewX * (-1);
        int secondYToAdd = 0;
        secondXToAdd = Math.round((float)(secondXToAdd * Math.cos(-m) - secondYToAdd * Math.sin(-m))) + this.vehicleX;
        secondYToAdd = Math.round((float)(secondXToAdd * Math.sin(-m) + secondXToAdd * Math.cos(-m))) + this.vehicleY;

        polygonVerticesX.add(new Integer(firstXToAdd));
        polygonVerticesX.add(new Integer(secondXToAdd));
        polygonVerticesY.add(new Integer(firstYToAdd));
        polygonVerticesY.add(new Integer(secondYToAdd));
        this.vehicleX = newX;
        this.vehicleY = newY;
        // int xVariation = newX - vehicleX;
        // int yVariation = newY - vehicleY;
        // for(int i = 0; i < polygonVerticesX.size(); i++) {
        //     polygonVerticesX.set(i, new Integer(polygonVerticesX.get(i) + xVariation));
        //     polygonVerticesY.set(i, new Integer(polygonVerticesY.get(i) + yVariation));
        // }
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
    
    public void drawSection(Coord c1, Coord c2, int diameter, boolean isFirst) {
        double cosAlpha = (c1.y - c2.y)/Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
        double senAlpha = (c1.x - c2.x)/Math.sqrt(Math.pow(c1.x - c2.x, 2) + Math.pow(c1.y - c2.y, 2));
    
        Coord p1 = new Coord((int) (c2.x + cosAlpha*(diameter/2)), (int ) (c2.y - senAlpha*(diameter/2)));
        Coord p2 = new Coord((int) (c2.x - cosAlpha*(diameter/2)), (int ) (c2.y + senAlpha*(diameter/2)));
        Coord p3 = new Coord((int) (c1.x - cosAlpha*(diameter/2)), (int ) (c1.y + senAlpha*(diameter/2)));
        Coord p4 = new Coord((int) (c1.x + cosAlpha*(diameter/2)), (int ) (c1.y - senAlpha*(diameter/2)));
        if(isFirst) {
            this.polygonVerticesX.add(new Integer(p3.x));
            this.polygonVerticesX.add(new Integer(p4.x));
            this.polygonVerticesY.add(new Integer(p3.y));
            this.polygonVerticesY.add(new Integer(p4.y));
        }
        this.polygonVerticesX.add(new Integer(p1.x));
        this.polygonVerticesY.add(new Integer(p1.y));
        this.polygonVerticesX.add(new Integer(p2.x));
        this.polygonVerticesY.add(new Integer(p2.y));
        // g.backColor = lineColor;
        // g.fillPolygon(new int[] {p1.x, p2.x, p3.x, p4.x}, new int[] {p1.y, p2.y, p3.y, p4.y}, 4);
        // g.backColor = 0;
        // g.drawThickLine(c1.x, c1.y, c2.x, c2.y, 5);
    }

}

