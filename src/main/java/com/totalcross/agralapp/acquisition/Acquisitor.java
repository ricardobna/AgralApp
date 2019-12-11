package com.totalcross.agralapp.acquisition;

import java.util.Vector;
import java.lang.Double;

public class Acquisitor {
    private Vector<Double> x = new Vector<>();
    private Vector<Double> y = new Vector<>();
    private int indexX = 0;
    private int indexY = 0;

    public void addX(Double value) {
        x.add(value);
    }

    public void addY(Double value) {
        y.add(value);
    }

    public Double getX() {
        return x.get(indexX);
    }

    public Double getY() {
        return y.get(indexY);
    }

    public Double previousX() {
        return x.get(indexX - 1);
    }

    public Double previousY() {
        return y.get(indexY - 1);
    }

    public Double originX() {
        return x.get(0);
    }

    public Double originY() {
        return y.get(0);
    }
}