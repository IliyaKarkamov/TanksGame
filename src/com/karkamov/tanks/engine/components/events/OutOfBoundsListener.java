package com.karkamov.tanks.engine.components.events;

import com.karkamov.tanks.engine.components.Position;

public class OutOfBoundsListener {
    public boolean onPositionChange(Position position) {
        return false;
    }
}
