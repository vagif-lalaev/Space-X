package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class EnemyShip extends Ship {
    public int score = 15;

    public EnemyShip(double x, double y) {
        super(x, y);
        setStaticView(ShapeMatrix.ENEMY);
    }

    public void move(Direction direction, double speed) { //движение по нажатию клавиш
        switch (direction) {
            case  RIGHT:
                x = x + speed;
                break;
            case LEFT:
                x = x - speed;
                break;
            case DOWN:
                y = y + 2;
                break;
        }
    }

    @Override
    public Bullet fire() {
        return new Bullet(x + 1, y + height, Direction.DOWN); //вылет пули
    }

    @Override
    public void kill() {
        if (isAlive) {
            isAlive = false;
            setAnimatedView(false,
                    ShapeMatrix.KILL_ENEMY_ANIMATION_FIRST,
                    ShapeMatrix.KILL_ENEMY_ANIMATION_SECOND,
                    ShapeMatrix.KILL_ENEMY_ANIMATION_THIRD);
        }
    }
}
