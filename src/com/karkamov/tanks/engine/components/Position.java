package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

import java.awt.*;

public class Position extends Component {
    public int x;
    public int y;

    private final Dimension size;

    private final Rectangle _bounds;

    public Position(Entity entity, int x, int y, Dimension size) {
        super(entity);

        this.x = x;
        this.y = y;

        this.size = size;

        _bounds = new Rectangle(x, y, size.width, size.height);
    }

    @Override
    public synchronized void update() {
        _bounds.setLocation(x, y);
    }

    public boolean intersects(Position position) {
        return _bounds.intersects(position.getRectangle());
    }

    public Rectangle getRectangle() {
        return _bounds;
    }

    public int left() {
        return x;
    }

    public int right() {
        return x + size.width;
    }

    public int top() {
        return y;
    }

    public int bottom() {
        return y + size.height;
    }
}
