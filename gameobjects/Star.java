package com.javarush.games.spaceinvaders.gameobjects;


import com.javarush.engine.cell.*;


public class Star extends GameObject {
    private static final String STAR_SIGN = "\u2606"; //символ звезды
    public Star(double x, double y) {
        super(x, y);
    }

    public void draw(Game game) {
        game.setCellValueEx((int)x, (int)y, Color.NONE, STAR_SIGN, Color.WHITE, 100); //рисуем звезду

    }

}
