package com.karkamov.tanks.engine.components.interfaces;

import com.karkamov.tanks.engine.entities.Entity;

import java.awt.*;

public abstract class Component {

    protected final Entity _entity;

    public Component(Entity entity) {
        _entity = entity;
    }

    public void init() {
    }

    public void update() {
    }

    public synchronized void draw(Graphics g) {
    }
}
