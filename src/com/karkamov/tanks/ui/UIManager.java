package com.karkamov.tanks.ui;

import com.karkamov.tanks.Game;
import com.karkamov.tanks.engine.Display;
import com.karkamov.tanks.engine.KeyboardListener;

import javax.swing.*;

public class UIManager {
    private Display _display;
    private Game _game;

    private final GamePanel _gamePanel;
    private final MenuPanel _menuPanel;
    private final KeyboardListener _keyboardListener;

    private JPanel _currentPanel;

    private UIState _state;
    private int _fps;

    public UIManager(Game game, Display display, KeyboardListener keyboardListener) {
        _display = display;
        _game = game;
        _keyboardListener = keyboardListener;

        _menuPanel = new MenuPanel(this, display);
        _gamePanel = new GamePanel(this, display, _keyboardListener);
    }

    public synchronized void drawCurrentState() {
        switch (_state) {
            case MENU:
                _display.add(_menuPanel);
                _currentPanel = _menuPanel;
                _game.setPlaying(false);
                break;

            case NEW_GAME:
                _display.add(_gamePanel);
                _currentPanel = _gamePanel;
                _game.setPlaying(true);
                break;

            case CONTINUE_GAME:
                _display.add(_gamePanel);
                _currentPanel = _gamePanel;
                _game.setPlaying(true);
                break;
        }
    }

    public synchronized void setState(UIState state) {
        if (_state != state && _currentPanel != null)
            _display.remove(_currentPanel);

        _state = state;
        drawCurrentState();
    }

    public void setFps(int fps) {
        _fps = fps;
    }

    public int getFps() {
        return _fps;
    }

    public void updateCurrentState() {
        if (_state == UIState.NEW_GAME || _state == UIState.CONTINUE_GAME) {
            _gamePanel.update();
        }
    }
}
