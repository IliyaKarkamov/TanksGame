package com.karkamov.tanks.map;

import com.karkamov.tanks.map.enums.EntityType;

public class Chunk {
    public EntityType entityType;

    public Chunk(EntityType entityType) {
        this.entityType = entityType;
    }
}
