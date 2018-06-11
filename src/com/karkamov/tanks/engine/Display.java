package com.karkamov.tanks.engine;

import javax.swing.*;
import java.awt.*;

public class Display extends JFrame {
    private static GraphicsEnvironment _graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();

    public Display(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setUndecorated(true);
        setAlwaysOnTop(true);

        GraphicsDevice defaultScreenDevice = _graphicsEnvironment.getDefaultScreenDevice();
        defaultScreenDevice.setFullScreenWindow(this);
        setFocusable(true);
        setVisible(false);
    }

    public GraphicsConfiguration getDefaultConfiguration() {
        return _graphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
    }
}