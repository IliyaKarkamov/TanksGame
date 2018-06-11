package com.karkamov.tanks.ui;

import com.karkamov.tanks.Main;
import com.karkamov.tanks.engine.Display;
import com.karkamov.tanks.engine.entities.EntityManager;
import com.karkamov.tanks.engine.KeyboardListener;
import com.karkamov.tanks.map.LevelCreator;
import com.karkamov.tanks.map.LevelReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GamePanel extends JPanel {
    private final Display _display;
    private final UIManager _uiManager;
    private final KeyboardListener _keyboardListener;

    private EntityManager _entityManager;
    private LevelCreator _levelCreator;

    private String _lastLevel;
    private boolean _playing = false;

    private BufferedImage _terrainImage = null;

    public GamePanel(UIManager uiManager, Display display, KeyboardListener keyboardListener) {
        _uiManager = uiManager;
        _display = display;
        _keyboardListener = keyboardListener;

        _entityManager = new EntityManager();
        _levelCreator = new LevelCreator(_entityManager, _display, _keyboardListener);

        _entityManager.init();

        setFocusable(false);

        try {
            _terrainImage = ImageIO.read(Main.class.getResource("resources/terrain.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!_playing)
            return;

        _entityManager.update();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = _display.getWidth();
        int height = _display.getHeight();

        // clear
        g.clearRect(0, 0, width, height);

        // draw
        g.drawImage(_terrainImage, 0, 0, width, height, null);

        _entityManager.draw(g);

        int offsetX = _levelCreator.getOffsetX();
        int offsetY = _levelCreator.getOffsetY();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, offsetY);
        g.fillRect(0, 0, offsetX, height);
        g.fillRect(0, height - offsetY, width, offsetY);
        g.fillRect(width - offsetX, 0, offsetX, height);

        g.setColor(Color.RED);
        g.drawString("fps: " + _uiManager.getFps(), width - 50, 20);
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
