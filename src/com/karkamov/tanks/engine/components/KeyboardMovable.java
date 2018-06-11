package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.engine.KeyboardListener;
import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.events.MoveDirectionListener;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

import java.awt.*;

public class KeyboardMovable extends Component {
    private final KeyboardListener _keyboardListener;

    private final int _topKey;
    private final int _leftKey;
    private final int _bottomKey;
    private final int _rightKey;

    private final Dimension _bounds;

    private Physics _physics;
    private MoveDirectionListener _moveDirectionListener = null;

    public KeyboardMovable(Entity entity, KeyboardListener keyboardListener,
                           int top, int left, int bottom, int right,
                           Dimension bounds) {
        super(entity);

        _keyboardListener = keyboardListener;

        _topKey = top;
        _leftKey = left;
        _bottomKey = bottom;
        _rightKey = right;

        _bounds = bounds;
    }

    @Override
    public void init() {
        _physics = (Physics) _entity.getComponent(Physics.class);
    }

    @Override
    public void update() {
        if (_keyboardListener.isKeyPressed(_topKey) && _physics.position.top() > 0) {
            _physics.setVelocity(0, -_physics.velocityY, MoveDirection.TOP);

            if (_moveDirectionListener != null)
                _moveDirectionListener.onDirectionChanged(MoveDirection.TOP);
        } else if (_keyboardListener.isKeyPressed(_leftKey) && _physics.position.left() > 0) {
            _physics.setVelocity(-_physics.velocityX, 0, MoveDirection.LEFT);

            if (_moveDirectionListener != null)
                _moveDirectionListener.onDirectionChanged(MoveDirection.LEFT);
        } else if (_keyboardListener.isKeyPressed(_bottomKey) && _physics.position.bottom() < _bounds.height) {
            _physics.setVelocity(0, _physics.velocityY, MoveDirection.BOTTOM);

            if (_moveDirectionListener != null)
                _moveDirectionListener.onDirectionChanged(MoveDirection.BOTTOM);
        } else if (_keyboardListener.isKeyPressed(_rightKey) && _physics.position.right() < _bounds.width) {
            _physics.setVelocity(_physics.velocityX, 0, MoveDirection.RIGHT);

            if (_moveDirectionListener != null)
                _moveDirectionListener.onDirectionChanged(MoveDirection.RIGHT);
        } else {
            _physics.setVelocity(0, 0, MoveDirection.BOTTOM);
        }
    }

    public void setMoveDirectionListener(MoveDirectionListener listener) {
        _moveDirectionListener = listener;
    }
}
