package com.totalcross.agralapp;
import totalcross.ui.MainWindow;
import totalcross.ui.gfx.Color;

import com.totalcross.agralapp.gfx.CameraContainer;

import totalcross.sys.Settings;
public class AgralApp extends MainWindow {
    
    public AgralApp() {
        setUIStyle(Settings.MATERIAL_UI);
    }

    @Override
    public void initUI() {
        CameraContainer cameraContainer = new CameraContainer();
        cameraContainer.setLineColor(Color.GREEN);
        add(cameraContainer, LEFT, TOP, SCREENSIZE, SCREENSIZE);
    }
}
