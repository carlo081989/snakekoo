package com.crowd.snakekoo.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Controls {

    private int currentDirection = 0; //0,1,2,3 U,R,D,L
    private int nextDirection = 0;
    private int yOffset = 0;
    private boolean isGoBack = false;

    private Vector2 touch = new Vector2();

    public Controls() {
        setControls();
    }

    public void setControls() {
        Gdx.input.setInputProcessor(new Gestures(new Gestures.DirectionListener() {

            @Override
            public void onUp() {
                if (Gdx.input.getY() < yOffset && currentDirection != 2) nextDirection = 0;
            }

            @Override
            public void onRight() {
                if (Gdx.input.getY() < yOffset && currentDirection != 3) nextDirection = 1;
            }

            @Override
            public void onDown() {
                if (Gdx.input.getY() < yOffset && currentDirection != 0) nextDirection = 2;
            }

            @Override
            public void onLeft() {
                if (Gdx.input.getY() < yOffset && currentDirection != 1) nextDirection =3;
            }
        }));
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    public int getDirection() {
        currentDirection = nextDirection;
        return  nextDirection;
    }

    public void update(Viewport viewport) {
        if (Gdx.input.isTouched()) {
            touch.x = Gdx.input.getX();
            touch.y = Gdx.input.getY();
            viewport.unproject(touch);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)
                && currentDirection != 2) nextDirection = 0;
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                && currentDirection != 3) nextDirection = 1;
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)
                && currentDirection != 0) nextDirection = 2;
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                && currentDirection != 1) nextDirection =3;
        else if (Gdx.input.isKeyPressed(Input.Keys.BACK)) isGoBack = true;
    }

    public boolean isGoBack() {
        return isGoBack;
    }

    public void setGoBack(boolean goBack) {
        isGoBack = goBack;
    }

    public void setyOffset(int yOffset) {
        this.yOffset = yOffset;
    }
}

