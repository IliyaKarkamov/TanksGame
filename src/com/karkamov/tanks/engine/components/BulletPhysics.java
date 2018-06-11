package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

import java.awt.*;

public class BulletPhysics extends Component {
    public Position position;
    public MoveDirection direction;

    public int velocity;
    public int currentVelocity;

    public BulletPhysics(Entity entity, Position position, int velocity, MoveDirection direction) {
        super(entity);

        this.position = position;
        this.velocity = velocity;
        this.direction = direction;

        this.currentVelocity = this.velocity;
    }

    @Override
    public void update() {
        if (position == null)
            return;

        switch (direction) {
            case TOP:
                position.y -= currentVelocity;
                break;
            case LEFT:
                position.x -= currentVelocity;
                break;
            case BOTTOM:
                position.y += currentVelocity;
                break;
            case RIGHT:
                position.x += currentVelocity;
                break;
        }

        position.update();
    }
}
