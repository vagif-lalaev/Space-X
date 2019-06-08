package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends GameObject {
    public boolean isAlive = true;
    private List<int[][]> frames; //список матриц для кадров
    private int frameIndex; //индекс текущего кадра
    private boolean loopAnimation = false;

    public Ship(double x, double y) {
        super(x, y);
    }

    public void setStaticView(int[][] viewFrame) {
        setMatrix(viewFrame);
        frames = new ArrayList<>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    public Bullet fire() {
        return null;
    }

    public void kill(){
        isAlive = false;
    }

    public void setAnimatedView(boolean isLoopAnimation, int[][] ... viewFrames) { //пост. анимация
        loopAnimation = isLoopAnimation;
        setMatrix(viewFrames[0]);
        frames = Arrays.asList(viewFrames);
        frameIndex = 0;
    }

    public void nextFrame() { //для переключ. на следующий кадр анимации
        frameIndex++;
        if (frameIndex < frames.size()) {
            for (int i = 0; i < frames.size(); i++) {
                matrix = frames.get(frameIndex);
            }
        } else if (frameIndex >= frames.size() && loopAnimation){
            frameIndex = 0;
        }


    }

    @Override
    public void draw(Game game) {
        super.draw(game);
        nextFrame();
    }

    public boolean isVisible() {
        return (!isAlive && frameIndex >= frames.size()) ? false : true;
    }
}
