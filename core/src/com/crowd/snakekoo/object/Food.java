package com.crowd.snakekoo.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.math.MathUtils;
import com.crowd.snakekoo.helper.ColorHelper;

public class Food {
    private int x;
    private int y;
    private Color color;

    public Food(int xBoardSize, int yBoardSize, Color color, boolean randomize) {
        if (randomize) {
            randomise(xBoardSize, yBoardSize);
        } else {
            this.color = color;
            x = xBoardSize;
            y = yBoardSize;
        }
    }

    public void randomise(int xBoardSize, int yBoardSize) {
        color = Colors.get(com.crowd.snakekoo.helper.ColorHelper.colors[MathUtils.random(ColorHelper.colors.length - 1)]);
        x = MathUtils.random(1, xBoardSize - 1);
        y = MathUtils.random(1, yBoardSize - 1);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return getX() == food.getX() &&
                getY() == food.getY();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

