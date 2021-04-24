package com.crowd.snakekoo.object;

import com.badlogic.gdx.graphics.Color;

public class BodyPart {
    private int x;
    private int y;
    private int colorId;
    private Color color;

    public BodyPart(int x, int y, int boardSize) {
        this.x = x % boardSize;
        if (this.x<0) this.x += boardSize;

        this.y = y % boardSize;
        if (this.y<0) this.y += boardSize;
    }

    public BodyPart(int x, int y, int xBoardSize, int yBoardSize, Color color) {
        this.x = x % xBoardSize;
        if (this.x < 0) this.x += xBoardSize;

        this.y = y % yBoardSize;
        if (this.y < 0) this.y += yBoardSize;

        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
}

