package com.karkamov.tanks.engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardListener implements KeyListener {
    private boolean _keys[] = new boolean[1024];

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        _keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        _keys[e.getKeyCode()] = false;
    }

    public boolean isKeyPressed(int keyCode) {
        return _keys[keyCode];
    }
}
