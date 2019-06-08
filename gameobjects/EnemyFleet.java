package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.*;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private Direction direction = Direction.RIGHT;
    private List<EnemyShip> ships;

    public EnemyFleet() {
        createShips();
    }

    private void createShips() { //заполним список вражескими кораблями
        ships = new ArrayList<>(ROWS_COUNT * COLUMNS_COUNT);
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
    }

    public void draw(Game game) { //отрисовка вражеского корабля
        for (EnemyShip enemyShip : ships) {
            enemyShip.draw(game);
        }
    }

    private double getLeftBorder() { //возвр. минимальную координату x
        List<Double> tempArr = new ArrayList<>();
        for (EnemyShip ship : ships) {
            tempArr.add(ship.x);
        }
        return Collections.min(tempArr);
    }

    private double getRightBorder() { //возвр. максимальную координату x + width
        List<Double> tempArr = new ArrayList<>();
        for (EnemyShip ship : ships) {
            tempArr.add(ship.x + ship.width);
        }
        return Collections.max(tempArr);
    }

    private double getSpeed() { //скорость движения корабля
        double value = 3.0 / ships.size();
        return 2.0 > value ? value : 2.0;
    }

    public void move() { //направление движение
        boolean changes = false;
        if (ships.size() != 0) {
            if (direction == Direction.LEFT && getLeftBorder() < 0) {
                direction = Direction.RIGHT;
                changes = true;
            } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
                direction = Direction.LEFT;
                changes = true;
            }
        }
        if (changes) {
            for (EnemyShip enemyShip : ships) {
                enemyShip.move(Direction.DOWN, getSpeed());
            }
        } else {
            for (EnemyShip enemyShip : ships) {
                enemyShip.move(direction, getSpeed());
            }
        }
    }

    public Bullet fire(Game game) { //огонь одного из враж.флота
        if (ships.size() == 0 || game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0)
            return null;
        return ships.get(game.getRandomNumber(ships.size())).fire();
    }

    public int verifyHit(List<Bullet> bullets) { //проверка пересечение корабля и пулей (каждый)
        int result = 0; //итог по очкам
        if (bullets.isEmpty()) {
            return 0;
        }
        for (EnemyShip enemyShip : ships) {
            for (Bullet bullet : bullets) {
                if (enemyShip.isCollision(bullet) && enemyShip.isAlive && bullet.isAlive) {
                    enemyShip.kill();
                    bullet.kill();
                    result += enemyShip.score;
                }
            }
        }
        return result;
    }

    public void deleteHiddenShips() {
        for (int i = 0; i < ships.size(); i++) {
            if (!ships.get(i).isVisible()) {
                ships.remove(i);
            }
        }
    }

    public double getBottomBorder() { //макс. (y + height)
        double result = ships.get(0).y + ships.get(0).height;
        for (EnemyShip ship : ships) {
            double height = ship.y + ship.height;
            if (height > result) {
                result = height;
            }
        }
        return result;
    }

    public int getShipsCount() {
        return ships.size();
    }
}
