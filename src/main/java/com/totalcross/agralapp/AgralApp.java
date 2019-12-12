package com.totalcross.agralapp;
import totalcross.ui.Button;
import totalcross.ui.MainWindow;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.icon.MaterialIcons;

import com.totalcross.agralapp.gfx.CameraContainer;

import totalcross.sys.Settings;
public class AgralApp extends MainWindow {
    
    public AgralApp() {
        setUIStyle(Settings.MATERIAL_UI);
        }

    @Override
    public void initUI() {
        Button buttonZoomIn = new Button(MaterialIcons._ZOOM_IN.toString());
        Button buttonZoomOut = new Button(MaterialIcons._ZOOM_OUT.toString());
        buttonZoomIn.setFont(Font.getFont(MaterialIcons._3D_ROTATION.fontName(), false, buttonZoomIn.getFont().size));
        buttonZoomOut.setFont(Font.getFont(MaterialIcons._3D_ROTATION.fontName(), false, buttonZoomIn.getFont().size));
        CameraContainer cameraContainer = new CameraContainer();
        cameraContainer.setLineColor(Color.GREEN);
        add(cameraContainer, LEFT, TOP, SCREENSIZE, SCREENSIZE);
        add(buttonZoomIn, RIGHT, TOP);
        add(buttonZoomOut, LEFT, TOP);
    }
}
