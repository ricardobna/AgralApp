package com.totalcross.agralapp.gfx;

import java.io.IOException;
import java.util.Vector;

import com.totalcross.agralapp.acquisition.Acquisitor;
import com.totalcross.agralapp.acquisition.ReadCSV;

import totalcross.ui.ClippedContainer;
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
        for (int i = 1; i < this.points.size(); i++) {
            g.drawLine(Math.round(this.points.getX(i - 1).floatValue()), Math.round(this.points.getY(i - 1).floatValue())
                     , Math.round(this.points.getX(i).floatValue()), Math.round(this.points.getY(i).floatValue()));
        }
        for(int i = 0; i < this.points.size(); i++) {
            updatePolygons(Math.round(this.points.getX(i).floatValue())
                         , Math.round(this.points.getY(i).floatValue()));
        }
        int oldColor = g.backColor;
        g.backColor = this.lineColor;
        g.fillPolygon(getVertices(polygonVerticesX), getVertices(polygonVerticesY), polygonVerticesX.size());
        g.backColor = oldColor;
    }

    private int[] getVertices(Vector<Integer> vertices) {
        int[] vertexes = new int[vertices.size()];
        for(int i = 0; i < vertices.size(); i++) {
            vertexes[i] = vertices.get(i);
        }
        return vertexes;
    }

    private void updatePolygons(int newX, int newY) {
        //Translocates the vehicle position to 0, and making the new point translocate the same amount
        int transformedNewX = newX - this.vehicleX;
        int transformedNewY = newY - this.vehicleY;
        //Getting the needed amount to scale to 10 DP
        float scaleX;
        float scaleY;
        if(transformedNewX != 0) {
            scaleX = UnitsConverter.toPixels(DP + 10)/transformedNewX;
        } else if(this.vehicleX != 0) {
            scaleX = UnitsConverter.toPixels(DP + 10)/this.vehicleX;
        } else {
            scaleX = UnitsConverter.toPixels(DP + 10);
        }
        if(transformedNewX != 0) {
            scaleY = UnitsConverter.toPixels(DP + 10)/transformedNewY;
        } else if(this.vehicleX != 0) {
            scaleY = UnitsConverter.toPixels(DP + 10)/this.vehicleY;
        } else {
            scaleY = UnitsConverter.toPixels(DP + 10);
        }
        //Rotating the point by m rad and scaling
        double m = Math.acos(((double)newY - this.vehicleY)/(newX - this.vehicleX));
        transformedNewX = Math.round((float)((transformedNewX * Math.cos(m) - transformedNewY * Math.sin(m))) * scaleX);
        transformedNewY = Math.round((float)((transformedNewX * Math.sin(m) + transformedNewY * Math.cos(m))) * scaleY);

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

}

