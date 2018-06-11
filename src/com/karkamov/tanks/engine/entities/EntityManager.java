package com.karkamov.tanks.engine.entities;

import java.awt.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class EntityManager {
    private ArrayList<Entity> _entities = new ArrayList<>();
    private Map<EntityGroup, ArrayList<Entity>> _groupedEntities = new EnumMap<>(EntityGroup.class);

    public void init() {
        for (Entity entity : _entities) {
            entity.init();
        }
    }

    public void update() {
        for (Entity entity : _entities) {
            entity.update();
        }

        refreshEntities();
    }

    public void draw(Graphics g) {
        for (Entity entity : _entities) {
            entity.draw(g);
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
}

