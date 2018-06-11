package com.karkamov.tanks.ui;

import com.karkamov.tanks.Main;
import com.karkamov.tanks.engine.Display;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuPanel extends JPanel {
    private final UIManager _uiManager;
    private final Display _display;

    private BufferedImage _backgroundImage = null;
    private BufferedImage _menuItemsBackgroundImage = null;

    private BufferedImage _continueImage = null;
    private BufferedImage _continueHoverImage = null;

    private BufferedImage _newGameImage = null;
    private BufferedImage _newGameHoverImage = null;

    private BufferedImage _quitImage = null;
    private BufferedImage _quitHoverImage = null;

    private JButton _continueButton;
    private JButton _newGameButton;
    private JButton _quitButton;

    private float _factorX;
    private float _factorY;

    public MenuPanel(UIManager uiManager, Display display) {
        _uiManager = uiManager;
        _display = display;

        setFocusable(false);

        try {
            _backgroundImage = ImageIO.read(Main.class.getResource("resources/MenuBackground.png"));
            _menuItemsBackgroundImage = ImageIO.read(Main.class.getResource("resources/MenuItemsBackground.png"));

            _continueImage = ImageIO.read(Main.class.getResource("resources/Continue.png"));
            _continueHoverImage = ImageIO.read(Main.class.getResource("resources/ContinueHover.png"));

            _newGameImage = ImageIO.read(Main.class.getResource("resources/NewGame.png"));
            _newGameHoverImage = ImageIO.read(Main.class.getResource("resources/NewGameHover.png"));

            _quitImage = ImageIO.read(Main.class.getResource("resources/Quit.png"));
            _quitHoverImage = ImageIO.read(Main.class.getResource("resources/QuitHover.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        _factorX = Math.abs(_backgroundImage.getWidth() / (float) _display.getWidth());
        _factorY = Math.abs(_backgroundImage.getHeight() / (float) _display.getHeight());

        _continueButton = new JButton(new ImageIcon(_continueImage));
        _newGameButton = new JButton(new ImageIcon(_newGameImage));
        _quitButton = new JButton(new ImageIcon(_quitImage));

        setMouseListeners();

        final int buttonsWidth = (int) (_continueImage.getWidth() / _factorX);
        final int buttonsHeight = (int) (_continueImage.getHeight() / _factorY);

        _continueButton.setContentAreaFilled(false);
        _continueButton.setBorderPainted(false);
        _continueButton.setPreferredSize(new Dimension(buttonsWidth, buttonsHeight));

        _newGameButton.setContentAreaFilled(false);
        _newGameButton.setBorderPainted(false);
        _newGameButton.setPreferredSize(new Dimension(buttonsWidth, buttonsHeight));

        _quitButton.setContentAreaFilled(false);
        _quitButton.setBorderPainted(false);
        _quitButton.setPreferredSize(new Dimension(buttonsWidth, buttonsHeight));

        setLayout(new BorderLayout());

        JPanel menuItemsPanel = new MenuItemsPanel();
        menuItemsPanel.setLayout(new GridBagLayout());

        final int menuItemsPanelWidth = (int) (_menuItemsBackgroundImage.getWidth() / _factorX);
        final int menuItemsPanelHeight = (int) (_menuItemsBackgroundImage.getHeight() / _factorY);

        menuItemsPanel.setPreferredSize(new Dimension(menuItemsPanelWidth, menuItemsPanelHeight));
        menuItemsPanel.setOpaque(false);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.add(Box.createVerticalStrut(50));
        buttonsPanel.add(_continueButton);
        buttonsPanel.add(Box.createVerticalStrut(20));
        buttonsPanel.add(_newGameButton);
        buttonsPanel.add(Box.createVerticalStrut(20));
        buttonsPanel.add(_quitButton);
        buttonsPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.weightx = 1;
        gbc.insets.left = menuItemsPanelWidth / 2 - buttonsWidth / 2;

        menuItemsPanel.add(buttonsPanel, gbc);
        add(menuItemsPanel, BorderLayout.SOUTH);
    }

    private void setMouseListeners() {
        _continueButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _uiManager.setState(UIState.CONTINUE_GAME);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                _continueButton.setIcon(new ImageIcon(_continueHoverImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                _continueButton.setIcon(new ImageIcon(_continueImage));
            }
        });

        _newGameButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                _uiManager.setState(UIState.NEW_GAME);
             }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                _newGameButton.setIcon(new ImageIcon(_newGameHoverImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                _newGameButton.setIcon(new ImageIcon(_newGameImage));
            }
        });

        _quitButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                _quitButton.setIcon(new ImageIcon(_quitHoverImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                _quitButton.setIcon(new ImageIcon(_quitImage));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(_backgroundImage, 0, 0, _display.getWidth(), _display.getHeight(), null);
    }

    private class MenuItemsPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            final int width = (int) (_menuItemsBackgroundImage.getWidth() / _factorX);
            final int height = (int) (_menuItemsBackgroundImage.getHeight() / _factorX);

            g.drawImage(_menuItemsBackgroundImage, 0, 0, width, height, null);
        }
    }
}
