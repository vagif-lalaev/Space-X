package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {
    private Direction direction = Direction.UP;

    public void setDirection(Direction newDirection) {
        if (newDirection != Direction.DOWN)
            this.direction = newDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length -1); //чтобы корабль отображался внизу экрана по центру
        setStaticView(ShapeMatrix.PLAYER); //внешний вид каробля
    }

    public void verifyHit(List<Bullet> bullets) { //попали ли вражеские пули в корабль игрока
        if (isAlive) {
            for (Bullet bullet : bullets) {
                if (bullet.isAlive && isCollision(bullet)) { //проверяет пересечение игрока с каждой из живых пуль
                    bullet.kill();
                    kill();
                }
            }
        }
    }

    @Override
    public void kill() {
        if (isAlive) {
            isAlive = false;
            setAnimatedView(false,  //анимация уничтожения корабля
                    ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD,
                    ShapeMatrix.DEAD_PLAYER);
        }
    }

    public void move() { //контроль в пределах игрового поля
        if (isAlive) {
            if (direction == Direction.LEFT)
                x--;
            if (direction == Direction.RIGHT)
                x++;
            if (x < 0)
                x = 0;
            if ((x + width) > SpaceInvadersGame.WIDTH)
                x = SpaceInvadersGame.WIDTH - width;
        }
    }

    @Override
    public Bullet fire() { //создание пули
        if (isAlive)
            return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
        else return null;
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }

}
