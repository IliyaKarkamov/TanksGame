package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.Main;
import com.karkamov.tanks.engine.Display;
import com.karkamov.tanks.engine.Transformation;
import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Image extends Component {
    private final Display _display;
    private final Dimension _dimension;
    private final String _resource;

    private Position _position;
    private BufferedImage _image = null;
    private BufferedImage _currentImage = null;

    public Image(Entity entity, Display display, Dimension dimension, String resource) {
        super(entity);

        _display = display;
        _dimension = dimension;
        _resource = resource;
    }

    @Override
    public void init() {
        try {
            _image = ImageIO.read(Main.class.getResource(_resource));
        } catch (IOException e) {
            e.printStackTrace();
        }

        _currentImage = _image;

        _position = (Position) _entity.getComponent(Position.class);
    }

    @Override
    public synchronized void draw(Graphics g) {
        g.drawImage(_currentImage, _position.x, _position.y, _dimension.width, _dimension.height, null);
    }

    public void setDirection(MoveDirection direction) {
        double angle = 2 * Math.PI;

        switch (direction) {
            case TOP:
                angle = Math.PI;
                break;
            case LEFT:
                angle = Math.PI / 2;
                break;
            case BOTTOM:
                angle = 2 * Math.PI;
                break;
            case RIGHT:
                angle = -Math.PI / 2;
                break;
        }

        _currentImage = Transformation.tilt(_image, angle, _display.getDefaultConfiguration());
    }
}
