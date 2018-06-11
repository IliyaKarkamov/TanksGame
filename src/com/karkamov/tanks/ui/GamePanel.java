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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {
    private final Display _display;
    private final UIManager _uiManager;
    private final KeyboardListener _keyboardListener;

    EntityManager _entityManager;

    public GamePanel(UIManager uiManager, Display display, KeyboardListener keyboardListener) {
        _uiManager = uiManager;
        _display = display;
        _keyboardListener = keyboardListener;

        _entityManager = new EntityManager();

        createPlayer();
        createDestroyableBrick();
        createUndestroyableBrick();
        createEnemy();

        _entityManager.init();

        setFocusable(false);
    }

    public void update() {
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

    private void createPlayer() {
        Entity player = _entityManager.createEntity();

        Dimension playerSize = new Dimension(60, 60);

        player.addComponent(new Position(player, 50, 50, playerSize));

        Physics physics = new Physics(player, 3, 3);
        physics.setOutOfBoundsListener(new OutOfBoundsListener() {
            @Override
            public boolean onPositionChange(Position position) {
                for (Entity entity : _entityManager.getAllEntities()) {
                    if (player != entity) {
                        Position entityPosition = (Position) entity.getComponent(Position.class);

                        if (position.intersects(entityPosition))
                            return true;
                    }
                }

                return false;
            }
        });

        player.addComponent(physics);

        Image imageComponent = new Image(player, _display, playerSize,
                "resources/entities/player.png");

        player.addComponent(imageComponent);

        KeyboardMovable keyboardMovableComponent = new KeyboardMovable(player, _keyboardListener,
                KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_DOWN, KeyEvent.VK_RIGHT,
                _display.getSize());

        player.addComponent(keyboardMovableComponent);

        KeyboardShooting keyboardShooting = new KeyboardShooting(player, _keyboardListener, _display.getSize());
        player.addComponent(keyboardShooting);

        keyboardMovableComponent.setMoveDirectionListener(new MoveDirectionListener() {
            @Override
            public void onDirectionChanged(MoveDirection direction) {
                imageComponent.setDirection(direction);
                keyboardShooting.setMoveDirection(direction);
            }
        });

        keyboardShooting.setBulletPositionListener(new BulletPositionListener() {
            @Override
            public boolean onPositionChanged(BulletPhysics physics) {
                for (Entity entity : _entityManager.getEntityGroup(EntityGroup.DESTROYABLE)) {
                    Position position = (Position) entity.getComponent(Position.class);

                    if (physics.position.intersects(position)) {
                        entity.destroy();
                        return true;
                    }
                }

                for (Entity entity : _entityManager.getEntityGroup(EntityGroup.UNDESTROYABLE)) {
                    Position position = (Position) entity.getComponent(Position.class);

                    if (physics.position.intersects(position)) {
                        return true;
                    }
                }

                return false;
            }
        });
    }

    private void createDestroyableBrick() {
        Entity brick = _entityManager.createEntity();

        Dimension playerSize = new Dimension(60, 60);

        brick.addComponent(new Position(brick, 200, 50, playerSize));
        Image imageComponent = new Image(brick, _display, playerSize,
                "resources/entities/destroyableBrick.png");

        brick.addComponent(imageComponent);

        _entityManager.addToGroup(EntityGroup.DESTROYABLE, brick);
        brick.setLives(3);
    }

    public void createUndestroyableBrick() {
        Entity brick = _entityManager.createEntity();

        Dimension brickSize = new Dimension(60, 60);

        brick.addComponent(new Position(brick, 500, 200, brickSize));
        Image imageComponent = new Image(brick, _display, brickSize,
                "resources/entities/undestroyableBrick.png");

        brick.addComponent(imageComponent);

        _entityManager.addToGroup(EntityGroup.UNDESTROYABLE, brick);
    }

    private void createEnemy() {
        Entity enemy = _entityManager.createEntity();

        Dimension playerSize = new Dimension(60, 60);

        enemy.addComponent(new Position(enemy, 150, 600, playerSize));
        enemy.addComponent(new Physics(enemy, 3, 3));

        Image imageComponent = new Image(enemy, _display, playerSize,
                "resources/entities/enemy.png");
        enemy.addComponent(imageComponent);
        _entityManager.addToGroup(EntityGroup.DESTROYABLE, enemy);
        enemy.setLives(3);
    }

}
