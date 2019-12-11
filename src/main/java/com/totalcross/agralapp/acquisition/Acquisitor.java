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
}