package com.totalcross.agralapp.acquisition;

import java.util.Vector;
import java.lang.Double;

public class Acquisitor {
    private Vector<Double> x = new Vector<>();
    private Vector<Double> y = new Vector<>();

    public void addX(Double value) {
        x.add(value);
    }

    public void addY(Double value) {
        y.add(value);
    }

    public Double getX(int index) {
        return x.get(index);
    }

    public Double getY(int index) {
        return y.get(index);
    }

    public Double originX() {
        return x.get(0);
    }

    public Double originY() {
        return y.get(0);
    }

    /***
     * Returns the size of the Vector, if x and y don't have the same amount of points it returns -1;
     * @return size
     */
    public int size() {
        return x.size() == y.size() ? x.size() : -1;
    }
}