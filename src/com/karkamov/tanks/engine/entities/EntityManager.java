package com.karkamov.tanks.engine.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class EntityManager {
    private ArrayList<Entity> _entities = new ArrayList<>();
    private Map<EntityGroup, ArrayList<Entity>> _groupedEntities = new EnumMap<>(EntityGroup.class);
    private boolean _gameOver;

    public void init() {
        for (Entity entity : _entities) {
            entity.init();
        }
    }

    public void update() {
        ArrayList<Entity> enemies = _groupedEntities.get(EntityGroup.ENEMY);

        if (enemies == null || enemies.isEmpty()) {
            _gameOver = true;
            return;
        }

        for (Entity entity : _entities) {
            entity.update();
        }

        refreshEntities();
    }

    public void draw(Graphics g) {
        ArrayList<Entity> undergroundDecoration =
                _groupedEntities.get(EntityGroup.UNDERGROUND_DECORATION);

        if (undergroundDecoration != null) {
            for (Entity entity : undergroundDecoration) {
                entity.draw(g);
            }
        }

        ArrayList<Entity> destroyable = _groupedEntities.get(EntityGroup.DESTROYABLE);

        if (destroyable != null) {
            for (Entity entity : destroyable) {
                entity.draw(g);
            }
        }

        ArrayList<Entity> undestroyable = _groupedEntities.get(EntityGroup.UNDESTROYABLE);

        if (undestroyable != null) {
            for (Entity entity : undestroyable) {
                entity.draw(g);
            }
        }

        ArrayList<Entity> abovegroundDecoration =
                _groupedEntities.get(EntityGroup.ABOVEGROUND_DECORATION);
        if (abovegroundDecoration != null) {
            for (Entity entity : abovegroundDecoration) {
                entity.draw(g);
            }
        }
    }

    private void refreshEntities() {
        for (ArrayList<Entity> entities : _groupedEntities.values())
            entities.removeIf(entity -> !entity.isAlive());

        _entities.removeIf(entity -> !entity.isAlive());
    }

    public Entity createEntity() {
        Entity entity = new Entity();
        _entities.add(entity);
        return entity;
    }

    public void addToGroup(EntityGroup group, Entity entity) {
        if (!_groupedEntities.containsKey(group))
            _groupedEntities.put(group, new ArrayList<>());

        _groupedEntities.get(group).add(entity);
    }

    public ArrayList<Entity> getEntityGroup(EntityGroup group) {
        return _groupedEntities.get(group);
    }

    public ArrayList<Entity> getAllEntities() {
        return _entities;
    }

    public void removeAll() {
        _entities.clear();
        _groupedEntities.clear();
    }

    public boolean isGameOver() {
        return _gameOver;
    }
}

