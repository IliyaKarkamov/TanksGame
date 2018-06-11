package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.engine.components.events.OutOfBoundsListener;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

public class Physics extends Component {

    public Position position;

    public int velocityX;
    public int velocityY;

    public int currentVelocityX = 0;
    public int currentVelocityY = 0;

    private OutOfBoundsListener _outOfBoundsListener;

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
    public synchronized void update() {
        Position newPos = new Position(_entity, position.x + currentVelocityX,
                position.y + currentVelocityY, position.getRectangle().getSize());

        if (_outOfBoundsListener != null &&
                _outOfBoundsListener.onPositionChange(newPos)) {

            currentVelocityX = 0;
            currentVelocityX = 0;
            return;
        }

        position.x += currentVelocityX;
        position.y += currentVelocityY;
    }

    public void setOutOfBoundsListener(OutOfBoundsListener outOfBoundsListener) {
        _outOfBoundsListener = outOfBoundsListener;
    }

    public void setVelocity(int x, int y) {
        Position newPos = new Position(_entity, position.x + x,
                position.y + y, position.getRectangle().getSize());

        if (_outOfBoundsListener != null &&
                _outOfBoundsListener.onPositionChange(newPos)) {

            currentVelocityX = 0;
            currentVelocityX = 0;
            return;
        }

        currentVelocityX = x;
        currentVelocityY = y;
    }
}
