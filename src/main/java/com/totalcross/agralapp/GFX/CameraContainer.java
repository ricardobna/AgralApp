package com.totalcross.agralapp.GFX;

import java.util.Vector;

import totalcross.ui.ClippedContainer;
import totalcross.ui.gfx.Graphics;

public class CameraContainer extends ClippedContainer {

    private Vector<Integer> polygonVerticesX = new Vector<>(20);
    private Vector<Integer> polygonVerticesY = new Vector<>(20);
    private int vehicleX;
    private int vehicleY;

    private int lineColor;

    @Override
    public void onPaint(Graphics g) {
        updatePolygons(); //this updates the polygons according with the camera
        g.fillPolygon(getVertices(polygonVerticesX), getVertices(polygonVerticesY), polygonVerticesX.size());
        
    }

    private int[] getVertices(Vector<Integer> vertices) {
        int[] vertexes = new int[vertices.size()];
        for(int i = 0; i < vertices.size(); i++) {
            vertexes[i] = vertices.get(i);
        }
        return vertexes;
    }

    private void updatePolygons() {
        int newX = 0;
        int newY = 0;
        //Updates the value with the GPS
        
        int xVariation = newX > vehicleX ? newX - vehicleX : vehicleX - newX;
        int yVariation = newY > vehicleY ? newY - vehicleY : vehicleY - newY;
        for(int i = 0; i < polygonVerticesX.size(); i++) {
            polygonVerticesX.set(i, new Integer(polygonVerticesX.get(i) + xVariation));
            polygonVerticesY.set(i, new Integer(polygonVerticesY.get(i) + yVariation));
        }
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}

