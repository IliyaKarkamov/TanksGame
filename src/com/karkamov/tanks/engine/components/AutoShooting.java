package com.karkamov.tanks.engine.components;

import com.karkamov.tanks.Main;
import com.karkamov.tanks.engine.components.enums.MoveDirection;
import com.karkamov.tanks.engine.components.events.BulletPositionListener;
import com.karkamov.tanks.engine.components.interfaces.Component;
import com.karkamov.tanks.engine.entities.Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class AutoShooting extends Component {
    private static final Dimension _bulletDimension = new Dimension(10, 10);
    private static final int _bulletVelocity = 4;
    private BufferedImage _bulletImage = null;

    private ArrayList<BulletPhysics> _bulletsPhysics = new ArrayList<>();
    private Physics _entityPhysics;
    private MoveDirection _entityDirection = MoveDirection.BOTTOM;

    private static final int _maxTicksUntilNewBullet = 65;
    private int _ticksUntilNewBullet = _maxTicksUntilNewBullet;

    private BulletPositionListener _bulletPositionListener;

    public AutoShooting(Entity entity) {
        super(entity);
    }

    @Override
    public void init() {
        try {
            _bulletImage = ImageIO.read(Main.class.getResource("resources/entities/enemy_bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        _entityPhysics = (Physics) _entity.getComponent(Physics.class);
    }

    @Override
    public synchronized void update() {
        if (_ticksUntilNewBullet >= _maxTicksUntilNewBullet) {
            _bulletsPhysics.add(getNewBulletPhysics());

            _ticksUntilNewBullet = 0;
        }

        _ticksUntilNewBullet++;

        for (BulletPhysics physics : _bulletsPhysics) {
            physics.update();
        }

        refreshBullets();
    }

    private void refreshBullets() {
        _bulletsPhysics.removeIf(physics -> _bulletPositionListener != null
                && _bulletPositionListener.onPositionChanged(physics));
    }

    @Override
    public synchronized void draw(Graphics g) {
        for (BulletPhysics physics : _bulletsPhysics) {
            g.drawImage(_bulletImage, physics.position.x, physics.position.y,
                    _bulletDimension.width, _bulletDimension.height, null);
        }
    }

    public void setMoveDirection(MoveDirection direction) {
        _entityDirection = direction;
    }

    private BulletPhysics getNewBulletPhysics() {
        int x = 0;
        int y = 0;

        switch (_entityDirection) {
            case TOP:
                x = (_entityPhysics.position.left() + _entityPhysics.position.right()) / 2;
                y = _entityPhysics.position.top();
                break;
            case LEFT:
                x = _entityPhysics.position.left();
                y = (_entityPhysics.position.top() + _entityPhysics.position.bottom()) / 2;
                break;
            case BOTTOM:
                x = (_entityPhysics.position.left() + _entityPhysics.position.right()) / 2;
                y = _entityPhysics.position.bottom();
                break;
            case RIGHT:
                x = _entityPhysics.position.right();
                y = (_entityPhysics.position.top() + _entityPhysics.position.bottom()) / 2;
                break;
        }

        return new BulletPhysics(null, new Position(null, x, y, _bulletDimension),
                _bulletVelocity, _entityDirection);
    }

    public void setBulletPositionListener(BulletPositionListener bulletPositionListener) {
        _bulletPositionListener = bulletPositionListener;
    }

}
