package com.karkamov.tanks;

import com.karkamov.tanks.engine.Display;
import com.karkamov.tanks.engine.KeyboardListener;
import com.karkamov.tanks.ui.UIManager;
import com.karkamov.tanks.ui.UIState;

import java.awt.event.KeyEvent;

public class Game {
    private Display _display;
    private UIManager _uiManager;
    private KeyboardListener _keyListener;

    private int _fps = 0;
    private boolean _running = false;
    private boolean _playing = false;

    public Game() {
        _display = new Display("Tanks game");
        _keyListener = new KeyboardListener();
        _uiManager = new UIManager(this, _display, _keyListener);

        _display.addKeyListener(_keyListener);
        _uiManager.setState(UIState.MENU);
    }

    public void run() {
        _running = true;

        double timePerTick = 1000000000 / 60;
        long lastTime = System.nanoTime();

        double delta = 0;
        long timer = 0;
        int ticks = 0;

        while (_running) {
            long now = System.nanoTime();

            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                update();

                ticks++;
                delta--;
            }

            render();

            if (timer >= 1000000000) {
                _fps = ticks;
                _uiManager.setFps(_fps);
                ticks = 0;
                timer = 0;
            }
        }
    }

    private void render() {
        // Draw
        _uiManager.drawCurrentState();

        //Show
        _display.repaint();
        _display.setVisible(true);
    }

    private void update() {
        if (_keyListener.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            _uiManager.setState(UIState.MENU);
        }

        _uiManager.updateCurrentState();
    }

    public void setPlaying(boolean playing) {
        _playing = playing;
    }
}
