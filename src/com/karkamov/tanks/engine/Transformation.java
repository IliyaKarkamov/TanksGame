package com.karkamov.tanks.engine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Transformation {
    public static BufferedImage tilt(BufferedImage image, double angle, GraphicsConfiguration gc) {
        double sin = Math.abs(Math.sin(angle));
        double cos = Math.abs(Math.cos(angle));

        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = (int) Math.floor(width * cos + height * sin);
        int newHeight = (int) Math.floor(height * cos + width * sin);

        int transparency = image.getColorModel().getTransparency();

        BufferedImage transformedImage = gc.createCompatibleImage(newWidth, newHeight, transparency);

        Graphics2D g = transformedImage.createGraphics();
        g.translate((newWidth - width) / 2, (newHeight - height) / 2);
        g.rotate(angle, width / 2, height / 2);
        g.drawRenderedImage(image, null);

        return transformedImage;
    }
}
