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

    private int _offsetX;
    private int _offsetY;

    public LevelCreator(EntityManager entityManager, Display display, KeyboardListener keyboardListener) {
        _entityManager = entityManager;
        _display = display;
        _keyboardListener = keyboardListener;
    }

    public void createLevel(LevelReader reader) {
        ArrayList<Chunk> chunks = reader.getChunks();

        int chunkPageSize = reader.getChunkPageSize();
        int currentChunk = 1;

        _offsetX = (_display.getWidth() % chunkPageSize) / 2;
        _offsetY = (_display.getHeight() % chunkPageSize) / 2;

        int x = _offsetX + 1;
        int y = _offsetY + 1;

        int chunkWidth = _display.getWidth() / chunkPageSize - 2;
        int chunkHeight = _display.getHeight() / chunkPageSize - 2;

        for (Chunk chunk : chunks) {
            switch (chunk.entityType) {
                case PLAYER:
                    createPlayer(x, y, chunkWidth - 5, chunkHeight - 5);
                    break;
                case ENEMY:
                    createEnemy(x, y, chunkWidth, chunkHeight);
                    break;
                case BRICK:
                    createBrick(x, y, chunkWidth, chunkHeight);
                    break;
                case UNDESTROYABLE_BRICK:
                    createUndestroyableBrick(x, y, chunkWidth, chunkHeight);
                    break;
                case GRASS:
                    createGrass(x, y, chunkWidth, chunkHeight);
                    break;
                case WATER:
                    createWater(x, y, chunkWidth, chunkHeight);
                    break;
            }

            if (currentChunk >= chunkPageSize) {
                currentChunk = 1;
                x = _offsetX + 1;
                y += chunkHeight + 2;
                continue;
            }

            x += chunkWidth + 2;

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
                ArrayList<Entity> destroyable = _entityManager.getEntityGroup(EntityGroup.DESTROYABLE);

                if (destroyable != null) {
                    for (Entity entity : destroyable) {
                        if (player != entity) {
                            Position entityPosition = (Position) entity.getComponent(Position.class);

                            if (position.intersects(entityPosition))
                                return true;
                        }
                    }
                }

                ArrayList<Entity> undestroyable = _entityManager.getEntityGroup(EntityGroup.UNDESTROYABLE);

                if (undestroyable != null) {
                    for (Entity entity : undestroyable)
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

        keyboardMovableComponent.setOffsetXY(_offsetX, _offsetY);

        player.addComponent(keyboardMovableComponent);

        KeyboardShooting keyboardShooting = new KeyboardShooting(player, _keyboardListener);
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
                if (physics.position.left() < _offsetX || physics.position.top() < _offsetY ||
                        physics.position.right() > _display.getWidth() - _offsetX ||
                        physics.position.bottom() > _display.getHeight() - _offsetY) {
                    return true;
                }

                ArrayList<Entity> shootThrough = _entityManager.getEntityGroup(EntityGroup.SHOOT_THROUGH);
                if (shootThrough != null) {
                    for (Entity entity : shootThrough) {
                        Position position = (Position) entity.getComponent(Position.class);

                        if (physics.position.intersects(position)) {
                            return false;
                        }
                    }
                }

                ArrayList<Entity> destroyable = _entityManager.getEntityGroup(EntityGroup.DESTROYABLE);
                if (destroyable != null) {
                    for (Entity entity : destroyable) {
                        if (entity == player)
                            continue;

                        Position position = (Position) entity.getComponent(Position.class);

                        if (physics.position.intersects(position)) {
                            entity.destroy();
                            return true;
                        }
                    }
                }

                ArrayList<Entity> undestroyable = _entityManager.getEntityGroup(EntityGroup.UNDESTROYABLE);
                if (undestroyable != null) {
                    for (Entity entity : undestroyable) {
                        Position position = (Position) entity.getComponent(Position.class);

                        if (physics.position.intersects(position)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        });

        player.setLives(3);
        _entityManager.addToGroup(EntityGroup.DESTROYABLE, player);
    }

    private void createEnemy(int x, int y, int chunkWidth, int chunkHeight) {
        Entity enemy = _entityManager.createEntity();

        Dimension playerSize = new Dimension(chunkWidth, chunkHeight);

        enemy.addComponent(new Position(enemy, x, y, playerSize));
        Physics physics = new Physics(enemy, 2, 2);

        enemy.addComponent(physics);

        Image imageComponent = new Image(enemy, _display, playerSize,
                "resources/entities/enemy.png");
        enemy.addComponent(imageComponent);

        AutoShooting autoShooting = new AutoShooting(enemy);
        autoShooting.setBulletPositionListener(new BulletPositionListener() {
            @Override
            public boolean onPositionChanged(BulletPhysics physics) {
                if (physics.position.left() < _offsetX || physics.position.top() < _offsetY ||
                        physics.position.right() > _display.getWidth() - _offsetX ||
                        physics.position.bottom() > _display.getHeight() - _offsetY) {
                    return true;
                }

                ArrayList<Entity> shootThrough = _entityManager.getEntityGroup(EntityGroup.SHOOT_THROUGH);
                if (shootThrough != null) {
                    for (Entity entity : shootThrough) {
                        Position position = (Position) entity.getComponent(Position.class);

                        if (physics.position.intersects(position)) {
                            return false;
                        }
                    }
                }

                ArrayList<Entity> destroyable = _entityManager.getEntityGroup(EntityGroup.DESTROYABLE);
                if (destroyable != null) {
                    for (Entity entity : destroyable) {
                        if (entity == enemy)
                            continue;

                        Position position = (Position) entity.getComponent(Position.class);

                        if (physics.position.intersects(position)) {
                            entity.destroy();
                            return true;
                        }
                    }
                }

                ArrayList<Entity> undestroyable = _entityManager.getEntityGroup(EntityGroup.UNDESTROYABLE);
                if (undestroyable != null) {
                    for (Entity entity : undestroyable) {
                        Position position = (Position) entity.getComponent(Position.class);

                        if (physics.position.intersects(position)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        });
        enemy.addComponent(autoShooting);

        AutoMovable autoMovable = new AutoMovable(enemy);
        autoMovable.setMoveDirectionListener(new MoveDirectionListener() {
            @Override
            public void onDirectionChanged(MoveDirection direction) {
                imageComponent.setDirection(direction);
                autoShooting.setMoveDirection(direction);
            }
        });
        enemy.addComponent(autoMovable);

        physics.setOutOfBoundsListener(new OutOfBoundsListener() {
            @Override
            public boolean onPositionChange(Position position) {
                if (position.left() < _offsetX || position.top() < _offsetY ||
                        position.right() > _display.getWidth() - _offsetX ||
                        position.bottom() > _display.getHeight() - _offsetY) {
                    return true;
                }

                ArrayList<Entity> destroyable = _entityManager.getEntityGroup(EntityGroup.DESTROYABLE);

                if (destroyable != null) {
                    for (Entity entity : destroyable) {
                        if (enemy != entity) {
                            Position entityPosition = (Position) entity.getComponent(Position.class);

                            if (position.intersects(entityPosition)) {
                                autoMovable.randomize();
                                return true;
                            }
                        }
                    }
                }

                ArrayList<Entity> undestroyable = _entityManager.getEntityGroup(EntityGroup.UNDESTROYABLE);

                if (undestroyable != null) {
                    for (Entity entity : undestroyable)
                        if (enemy != entity) {
                            Position entityPosition = (Position) entity.getComponent(Position.class);

                            if (position.intersects(entityPosition)) {
                                autoMovable.randomize();
                                return true;
                            }
                        }
                }

                return false;
            }
        });

        enemy.setLives(3);

        _entityManager.addToGroup(EntityGroup.DESTROYABLE, enemy);
        _entityManager.addToGroup(EntityGroup.ENEMY, enemy);
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

    private void createGrass(int x, int y, int chunkWidth, int chunkHeight) {
        Entity brick = _entityManager.createEntity();

        Dimension brickSize = new Dimension(chunkWidth, chunkHeight);

        brick.addComponent(new Position(brick, x, y, brickSize));
        Image imageComponent = new Image(brick, _display, brickSize,
                "resources/entities/grass.png");

        brick.addComponent(imageComponent);

        _entityManager.addToGroup(EntityGroup.ABOVEGROUND_DECORATION, brick);
    }

    private void createWater(int x, int y, int chunkWidth, int chunkHeight) {
        Entity water = _entityManager.createEntity();

        Dimension brickSize = new Dimension(chunkWidth, chunkHeight);

        water.addComponent(new Position(water, x, y, brickSize));
        Image imageComponent = new Image(water, _display, brickSize,
                "resources/entities/water.png");

        water.addComponent(imageComponent);

        _entityManager.addToGroup(EntityGroup.UNDESTROYABLE, water);
        _entityManager.addToGroup(EntityGroup.SHOOT_THROUGH, water);
    }

    public int getOffsetX() {
        return _offsetX;
    }

    public int getOffsetY() {
        return _offsetY;
    }
}
