package com.karkamov.tanks.map;

import com.karkamov.tanks.engine.Display;
import com.karkamov.tanks.engine.KeyboardListener;
import com.karkamov.tanks.engine.components.*;
import com.karkamov.tanks.engine.components.Image;
import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.events.BulletPositionListener;
import com.karkamov.tanks.engine.components.events.MoveDirectionListener;
import com.karkamov.tanks.engine.components.events.OutOfBoundsListener;
import com.karkamov.tanks.engine.entities.Entity;
import com.karkamov.tanks.engine.entities.EntityGroup;
import com.karkamov.tanks.engine.entities.EntityManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class LevelCreator {
    private final EntityManager _entityManager;
    private final Display _display;
    private final KeyboardListener _keyboardListener;

    public LevelCreator(EntityManager entityManager, Display display, KeyboardListener keyboardListener) {
        _entityManager = entityManager;
        _display = display;
        _keyboardListener = keyboardListener;
    }

    public void createLevel(LevelReader reader) {
        ArrayList<Chunk> chunks = reader.getChunks();

        int chunkPageSize = reader.getChunkPageSize();
        int currentChunk = 0;

        int x = 0;
        int y = 0;

        int chunkWidth = _display.getWidth() / chunkPageSize;
        int chunkHeight = _display.getHeight() / chunkPageSize;

        for (Chunk chunk : chunks) {
            switch (chunk.entityType) {
                case Player:
                    createPlayer(x, y, chunkWidth, chunkHeight);
                    break;
                case Enemy:
                    createEnemy(x, y, chunkWidth, chunkHeight);
                    break;
                case Brick:
                    createBrick(x, y, chunkWidth, chunkHeight);
                    break;
                case UndestroyableBrick:
                    createUndestroyableBrick(x, y, chunkWidth, chunkHeight);
                    break;
            }

            if (currentChunk >= chunkPageSize) {
                currentChunk = 0;
                x= 0;
                y += chunkHeight;
            }

            x += chunkWidth;

            currentChunk++;
        }

    }

    private void createPlayer(int x, int y, int chunkWidth, int chunkHeight) {
        Entity player = _entityManager.createEntity();

        Dimension playerSize = new Dimension(chunkWidth, chunkHeight);

        player.addComponent(new Position(player, x, y, playerSize));

        Physics physics = new Physics(player, 3, 3);
        physics.setOutOfBoundsListener(new OutOfBoundsListener() {
            @Override
            public boolean onPositionChange(Position position) {
                for (Entity entity : _entityManager.getAllEntities())
                    if (player != entity) {
                        Position entityPosition = (Position) entity.getComponent(Position.class);

                        if (position.intersects(entityPosition))
                            return true;
                    }

                return false;
            }
        });

        player.addComponent(physics);

        com.karkamov.tanks.engine.components.Image imageComponent = new Image(player, _display, playerSize,
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

    private void createEnemy(int x, int y, int chunkWidth, int chunkHeight) {
        Entity enemy = _entityManager.createEntity();

        Dimension playerSize = new Dimension(chunkWidth, chunkHeight);

        enemy.addComponent(new Position(enemy, x, y, playerSize));
        enemy.addComponent(new Physics(enemy, 3, 3));

        Image imageComponent = new Image(enemy, _display, playerSize,
                "resources/entities/enemy.png");
        enemy.addComponent(imageComponent);
        enemy.setLives(3);

        _entityManager.addToGroup(EntityGroup.DESTROYABLE, enemy);
    }

    private void createBrick(int x, int y, int chunkWidth, int chunkHeight) {
        Entity brick = _entityManager.createEntity();

        Dimension playerSize = new Dimension(chunkWidth, chunkHeight);

        brick.addComponent(new Position(brick, x, y, playerSize));
        Image imageComponent = new Image(brick, _display, playerSize,
                "resources/entities/destroyableBrick.png");

        brick.addComponent(imageComponent);
        brick.setLives(3);

        _entityManager.addToGroup(EntityGroup.DESTROYABLE, brick);
    }

    private void createUndestroyableBrick(int x, int y, int chunkWidth, int chunkHeight) {
        Entity brick = _entityManager.createEntity();

        Dimension brickSize = new Dimension(chunkWidth, chunkHeight);

        brick.addComponent(new Position(brick, x, y, brickSize));
        Image imageComponent = new Image(brick, _display, brickSize,
                "resources/entities/undestroyableBrick.png");

        brick.addComponent(imageComponent);

        _entityManager.addToGroup(EntityGroup.UNDESTROYABLE, brick);
    }
}
