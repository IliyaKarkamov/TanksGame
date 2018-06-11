package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.events.OutOfBoundsListener;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

public class Physics extends Component {

    public Position position;
    public MoveDirection direction;

    public int velocityX;
    public int velocityY;

    public int currentVelocityX = 0;
    public int currentVelocityY = 0;

    private OutOfBoundsListener _outOfBoundsListener;
    private boolean _intersecting = false;
    public MoveDirection _intersectionDirection;

    public Physics(Entity entity, int velocity_x, int velocity_y) {
        super(entity);

        velocityX = velocity_x;
        velocityY = velocity_y;
    }

    @Override
    public void init() {
        position = (Position) _entity.getComponent(Position.class);
    }

    @Override
    public void update() {
        if (_outOfBoundsListener != null &&
                _outOfBoundsListener.onPositionChange(position)) {
            if (_intersecting && _intersectionDirection != direction) {
                position.x += currentVelocityX;
                position.y += currentVelocityY;
                return;
            }

            _intersecting = true;
            _intersectionDirection = direction;
            return;
        }

        _intersecting = false;

        position.x += currentVelocityX;
        position.y += currentVelocityY;
    }

    public void setOutOfBoundsListener(OutOfBoundsListener outOfBoundsListener) {
        _outOfBoundsListener = outOfBoundsListener;
    }

    public void setVelocity(int x, int y, MoveDirection direction) {
        currentVelocityX = x;
        currentVelocityY = y;
        this.direction = direction;
    }
}
