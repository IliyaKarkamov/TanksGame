package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.events.MoveDirectionListener;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

import java.util.concurrent.ThreadLocalRandom;

public class AutoMovable extends Component {
    private Physics _physics;

    private MoveDirection _direction;
    private int _steps;

    private MoveDirectionListener _moveDirectionListener;

    public AutoMovable(Entity entity) {
        super(entity);
    }

    @Override
    public void init() {
        _physics = (Physics) _entity.getComponent(Physics.class);
    }

    @Override
    public synchronized void update() {
        if (_steps <= 0) {
            randomize();
        }

        switch (_direction) {
            case TOP:
                _physics.setVelocity(0, -_physics.velocityY);
                break;
            case LEFT:
                _physics.setVelocity(-_physics.velocityY, 0);
                break;
            case BOTTOM:
                _physics.setVelocity(0, _physics.currentVelocityY);
                break;
            case RIGHT:
                _physics.setVelocity(_physics.velocityY, 0);
                break;
        }

        _steps--;
    }

    public void randomize() {
        _direction = MoveDirection.values()[ThreadLocalRandom.current().nextInt(0, 4)];
        _steps = ThreadLocalRandom.current().nextInt(40, 65);

        if (_moveDirectionListener != null)
            _moveDirectionListener.onDirectionChanged(_direction);
    }

    public void setMoveDirectionListener(MoveDirectionListener moveDirectionListener) {
        _moveDirectionListener = moveDirectionListener;
    }
}
