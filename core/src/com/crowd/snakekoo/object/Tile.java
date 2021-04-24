package com.crowd.snakekoo.object;

import com.badlogic.gdx.graphics.Color;

public class Tile {
    private float x;
    private float y;
    private float width;
    private float height;
    private Color color;
    private boolean isTouched;

    public Tile (float x, float y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Tile(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void setTouched(boolean touched) {
        isTouched = touched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        Tile tile = (Tile) o;
        return Float.compare(getX(), tile.getX() * tile.getWidth()) >= 0 &&
                Float.compare(getY(), tile.getY() * tile.getHeight()) >= 0 &&
                Float.compare(getX(), (tile.getX() * tile.getWidth()) + tile.getWidth()) <= 0 &&
                Float.compare(getY(), (tile.getY() * tile.getHeight()) + tile.getHeight()) <= 0;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
