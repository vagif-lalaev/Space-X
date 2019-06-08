package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64 ;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5; //слож.игры
    private EnemyFleet enemyFleet;
    private List<Star> stars; //список звезд
    private List<Bullet> enemyBullets; //cписок пуль врага
    private PlayerShip playerShip;
    private boolean isGameStopped; //остановка
    private int animationsCount;
    private List<Bullet> playerBullets; //список пуль
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score;

    public void initialize() {
        setScreenSize(WIDTH, HEIGHT); //размер игравого поля
        createGame();
    }

    private void drawField() { //отрисовки
        for (int y = 0; y < HEIGHT; y++) { //фон
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        for (Star star : stars) { //звезды
            star.draw(this);
        }
    }

    private void createGame() { //создания всех элементов в игре
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        playerBullets = new ArrayList<>();
        drawScene();
        setTurnTimer(40); //частота работы метода onTurn
        score = 0;
    }

    private void drawScene() { //отрисовка элементов
        drawField();
        for (Bullet playerBullet : playerBullets)
            playerBullet.draw(this);
        playerShip.draw(this);
        for (Bullet bullet : enemyBullets)
            bullet.draw(this);
        enemyFleet.draw(this);
    }

    private void createStars() { //для заполнения список новыми звездами
        stars = new ArrayList<>();
        for (int z = 0; z < 17; z++) {
            stars.add(new Star(Math.random() * WIDTH, Math.random() * HEIGHT));
        }
    }

    @Override
    public void onTurn(int step) { // периодическая перерисовка экрана
        moveSpaceObjects();
        check();
        Bullet enemy = enemyFleet.fire(this);
        if (enemy != null)
            enemyBullets.add(enemy);
        setScore(score);
        drawScene();
    }

    private void moveSpaceObjects() { //движение объектов
        enemyFleet.move();
        for (Bullet bullet : enemyBullets)
            bullet.move();
        playerShip.move();
        for (Bullet playerBullet : playerBullets)
            playerBullet.move();
    }

    private void removeDeadBullets() {
        for (int i = 0; i < enemyBullets.size() ; i++) {
            if (!enemyBullets.get(i).isAlive || enemyBullets.get(i).y >= HEIGHT - 1)
                enemyBullets.remove(i);
        }
        playerBullets.removeIf(bullet -> !bullet.isAlive || (bullet.y + bullet.height) < 0); //вылетают за пределы поля
    }

    private void check() {
        if (enemyFleet.getBottomBorder() >= playerShip.y)
            playerShip.kill();
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
        playerShip.verifyHit(enemyBullets);
        enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (!playerShip.isAlive)
            stopGameWithDelay();
        score += enemyFleet.verifyHit(playerBullets);
    }

    private void stopGame(boolean isWin) { //остановка игры
        isGameStopped = true;
        stopTurnTimer();
        if (isWin)
            showMessageDialog(Color.NONE, "YOU WIN", Color.GREEN, 20); //победа
        else showMessageDialog(Color.NONE, "THE END", Color.RED, 20);//порожение
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10)
            stopGame(playerShip.isAlive);
    }

    @Override
    public void onKeyPress(Key key) { //напровление движения
        if (isGameStopped && key == Key.SPACE) {
            createGame();
        } else if (key == Key.LEFT) {
            playerShip.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            playerShip.setDirection(Direction.RIGHT);
        }
        if (key == Key.SPACE ) {
            Bullet bullet = playerShip.fire();
            if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(bullet);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) { //прекращает движение
        if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT)
            playerShip.setDirection(Direction.UP);
        if (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT)
            playerShip.setDirection(Direction.UP);
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return;
        }
        super.setCellValueEx(x, y, cellColor, value);
    }
}
