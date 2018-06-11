package com.karkamov.tanks.ui;

import com.karkamov.tanks.engine.Display;
import com.karkamov.tanks.engine.components.*;
import com.karkamov.tanks.engine.components.Image;
import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.events.BulletPositionListener;
import com.karkamov.tanks.engine.components.events.MoveDirectionListener;
import com.karkamov.tanks.engine.components.events.OutOfBoundsListener;
import com.karkamov.tanks.engine.entities.Entity;
import com.karkamov.tanks.engine.entities.EntityGroup;
import com.karkamov.tanks.engine.entities.EntityManager;
import com.karkamov.tanks.engine.KeyboardListener;
import com.karkamov.tanks.map.LevelCreator;
import com.karkamov.tanks.map.LevelReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final Display _display;
    private final UIManager _uiManager;
    private final KeyboardListener _keyboardListener;

    private EntityManager _entityManager;
    private LevelCreator _levelCreator;

    private String _lastLevel;
    private boolean _playing = false;

    public GamePanel(UIManager uiManager, Display display, KeyboardListener keyboardListener) {
        _uiManager = uiManager;
        _display = display;
        _keyboardListener = keyboardListener;

        _entityManager = new EntityManager();
        _levelCreator = new LevelCreator(_entityManager, _display, _keyboardListener);

        _entityManager.init();

        setFocusable(false);
    }

    public void update() {
        if (!_playing)
            return;

        _entityManager.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // clear
        g.clearRect(0, 0, _display.getWidth(), _display.getHeight());

        // draw
        g.setColor(Color.BLACK);
        g.drawString("fps: " + _uiManager.getFps(), _display.getWidth() - 50, 20);

        _entityManager.draw(g);
    }

    public void setLastLevel(String lastLevel) {
        _lastLevel = lastLevel;
    }

    public void start() {
        if (_playing)
            return;

        _entityManager.removeAll();

        LevelReader levelReader = new LevelReader();
        levelReader.parseLevel(_lastLevel);

        _levelCreator.createLevel(levelReader);

        _entityManager.init();

        _playing = true;
    }
}
