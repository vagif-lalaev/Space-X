package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class Bullet extends GameObject {
    private int dy;
    public boolean isAlive = true; //жизнь пули

    public Bullet(double x, double y, Direction direction) {
        super(x, y);
        setMatrix(ShapeMatrix.BULLET);

        switch (direction) {
            case UP:
                dy = -1;
                break;
            default:
                dy = 1;
        }
    }

    public void move(){
        y = y + dy;
    }

    public void kill() {
        isAlive = false;
    }
}
