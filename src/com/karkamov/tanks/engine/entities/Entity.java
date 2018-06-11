package com.karkamov.tanks.engine.entities;

import com.karkamov.tanks.engine.components.interfaces.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Entity {
    private Map<Class<?>, Component> _components = new HashMap<>();

    int _lives = 1;

    public void init() {
        for (Component component : _components.values()) {
            component.init();
        }
    }

    public void update() {
        for (Component component : _components.values()) {
            component.update();
        }
    }

    public void draw(Graphics g) {
        for (Component component : _components.values()) {
            component.draw(g);
        }
    }

    public void addComponent(Component component) {
        _components.put(component.getClass(), component);
    }

    public Component getComponent(Class<?> classType) {
        if (_components.containsKey(classType))
            return _components.get(classType);

        return null;
    }

    public boolean isAlive() {
        return _lives > 0;
    }

    public void destroy() {
        _lives--;
    }

    public void setLives(int lives) {
        _lives = lives;
    }
}
