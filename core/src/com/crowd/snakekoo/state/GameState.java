package com.crowd.snakekoo.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crowd.snakekoo.control.Controls;
import com.crowd.snakekoo.helper.ColorHelper;
import com.crowd.snakekoo.helper.FontHelper;
import com.crowd.snakekoo.object.BodyPart;
import com.crowd.snakekoo.object.Food;
import com.crowd.snakekoo.object.Tile;

public class GameState {
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();
    private Controls controls = new Controls();
    private Vector3 touch;
    private BitmapFont scoreFont;
    private BitmapFont timerFont;
    private Sound sound;

    private Queue<BodyPart> mBody = new Queue<>();
    private Array<Food> mFoods = new Array<>();
    private Array<Tile> tiles = new Array<>();
    private Array<Tile> touchedTiles = new Array<>();

    private Color bodyColor;
    private Color deltaColor = null;

    private int xBoardSize; //How many squares in the board
    private int yBoardSize;
    private int yOffset; //How high the board is off the bottom
    private int snakeLength = 3;
    private int score;
    private int timer;

    private float speed;
    private float speedTimer = 0;
    private float colourDuration = 15;
    private float colourCounter = 0;

    private boolean isMatched = true;
    private boolean isSubmit = false;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private boolean isFoodExist = false;

    public GameState(int width, int height, int food, float speed) {

        xBoardSize = (int) Math.round(width / (width * 0.05));
        yOffset = (int) Math.round(height * 0.20);
        yBoardSize = (int) Math.round((height - yOffset) / (height * 0.025));

        bodyColor = Color.WHITE;
        mBody.addLast(new BodyPart(15,15,xBoardSize, yBoardSize, bodyColor)); //head
        mBody.addLast(new BodyPart(15,14,xBoardSize, yBoardSize, bodyColor));
        mBody.addLast(new BodyPart(15,13,xBoardSize, yBoardSize, bodyColor));

        for (int x = 0 ; x <= food ; x++) {
            addFood();
        }

        for (int x = 0; x < xBoardSize/2; x++) {
            for (int y = 0; y < (yOffset/(height * 0.03))/2; y++) {
                tiles.add(new Tile(x, y, Colors.get(ColorHelper
                        .colors[MathUtils.random(ColorHelper.colors.length - 1)])));
            }
        }

        touch = new Vector3();
        sound = Gdx.audio.newSound(Gdx.files.internal("button-3.wav"));
        scoreFont = FontHelper.getFont(50, Color.WHITE, false);
        timerFont = FontHelper.getFont(50, Color.WHITE, false);
        this.speed = speed;
        controls.setyOffset(height - yOffset);
    }

    public void update(float delta, Viewport viewport) { //update game logic
        speedTimer += delta;
        colourCounter += delta;
        controls.update(viewport);
        if (controls.isGoBack()) {
            controls.setGoBack(false);
            setPaused(true);
        }
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)
                && Gdx.input.getRotation() == 0
                && isTouched(viewport.getCamera())) {
            if (touchedTiles.size > 0 && (!isMatched || isSubmit)) {
                for (Tile tile : touchedTiles) {
                    tile.setTouched(false);
                }

                if (!isMatched) {
                    isMatched = true;
                }

                if (touchedTiles.size >= 3 && isSubmit) {
                    colourDuration = (touchedTiles.size * 2) + 2;
                    colourCounter = 0;
                    bodyColor = deltaColor;
                    timerFont = FontHelper.getFont(50, bodyColor, false);
                    isSubmit = false;
                    for (Tile tile : touchedTiles) {
                        tile.setColor(Colors.get(ColorHelper.colors[MathUtils.random(ColorHelper.colors.length - 1)]));
                    }
                }
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                deltaColor = null;
                touchedTiles.clear();
            }
            if (isGameOver) isGameOver = false;
        }
        //0.05 = fastest
        //0.50 = slowest
        if (speedTimer > speed * 0.05) {
            speedTimer = 0;
            if (!isGameOver) advance();
        }

        timer = (int) (colourDuration - colourCounter);

        if (colourDuration < colourCounter) {
            colourCounter = 0;
            colourDuration = 0;
            bodyColor = Color.WHITE;
        }
    }

    private void advance() {
        int headX = mBody.first().getX();
        int headY = mBody.first().getY();

        switch(controls.getDirection()) {
            case 0: //up
                mBody.addFirst(new BodyPart(headX, headY+1, xBoardSize, yBoardSize, bodyColor));
                break;
            case 1: //right
                mBody.addFirst(new BodyPart(headX+1, headY, xBoardSize, yBoardSize, bodyColor));
                break;
            case 2: //down
                mBody.addFirst(new BodyPart(headX, headY-1, xBoardSize, yBoardSize, bodyColor));
                break;
            case 3: //left
                mBody.addFirst(new BodyPart(headX-1, headY, xBoardSize, yBoardSize, bodyColor));
                break;
            default://should never happen
                mBody.addFirst(new BodyPart(headX, headY+1, xBoardSize, yBoardSize, bodyColor));
                break;
        }

        Food tempFood = new Food(mBody.first().getX(), mBody.first().getY(), mBody.first().getColor(), false);
        if (mFoods.contains(tempFood, false)) {
            tempFood = mFoods.get(mFoods.indexOf(tempFood, false));
            if ((mBody.first().getColor().equals(Color.WHITE) && mBody.last().getColor().equals(tempFood.getColor()))
                    || (mBody.first().getColor().equals(tempFood.getColor()))) {
                snakeLength++;
                score += (11 - speed) * mFoods.size;
                sound.play();
                mFoods.removeValue(tempFood, false);
                if (mFoods.size < 20) {
                    addFood();
                }
            } else {
                if (!mBody.first().getColor().equals(Color.WHITE)) isGameOver = true;
            }
            //mFood.randomisePos(xBoardSize, yBoardSize); //TODO check it's not in body
        }

        for (int i = 1; i<mBody.size; i++) {
            if (mBody.get(i).getX() == mBody.first().getX()
                    && mBody.get(i).getY() == mBody.first().getY()) {
                isGameOver = true;
            }
        }

        while (mBody.size - 1 >= snakeLength) {
            mBody.removeLast();
        }

        if (bodyColor.equals(Color.WHITE) && timer <= 0) score--;

        if (score <= -100 || mFoods.isEmpty()) isGameOver = true;
    }

    public boolean isTouched (Camera camera) {
        if (Gdx.input.isTouched()) {
            isSubmit = false;
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            Tile tempTile = new Tile(touch.x, touch.y,null);
            if (tiles.contains(tempTile, false)) {
                if (!touchedTiles.contains(tempTile, false)) {
                    Tile tile = tiles.get(tiles.indexOf(tempTile, false));
                    if (deltaColor == null) {
                        deltaColor = tile.getColor();
                    }
                    if (deltaColor.equals(tile.getColor())) {
                        tile.setTouched(true);
                        touchedTiles.add(tile);
                    } else {
                        isMatched = false;
                    }
                }
            } else {
                if (touch.y > yOffset) isSubmit = true;
            }
            return true;
        }
        return false;
    }

    public void draw(int width, int height, OrthographicCamera camera) { //draw snake and board
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        /* Draw the border outline for the play area*/
        shapeRenderer.setColor(Color.CLEAR);
        shapeRenderer.rect(0, yOffset, width, height-yOffset);

        /* Set the background color of the play area to black */
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0+5, yOffset+5, width-5*2, (height-yOffset)-5*2);

        float xScaleSnake = width/xBoardSize; //width of one board square
        float yScaleSnake = (height-yOffset)/yBoardSize;

        for (Food food : mFoods) {
            shapeRenderer.setColor(food.getColor());
            shapeRenderer.circle(food.getX() * xScaleSnake, food.getY()*yScaleSnake + yOffset, xScaleSnake/2);
        }

        for (BodyPart bp : mBody) { //snake
            shapeRenderer.setColor(bp.getColor());
            shapeRenderer.circle(bp.getX()*xScaleSnake, bp.getY()*yScaleSnake + yOffset, xScaleSnake/2);
        }

        for (Tile tile : tiles) {
            shapeRenderer.setColor(tile.getColor());
            tile.setWidth(xScaleSnake * 2);
            tile.setHeight(yScaleSnake * 2);
            if (tile.isTouched()) {
                shapeRenderer.rect(tile.getX()*xScaleSnake*2, tile.getY()*yScaleSnake*2, tile.getWidth(),tile.getHeight());
            } else {
                shapeRenderer.ellipse(tile.getX() * xScaleSnake * 2, tile.getY() * yScaleSnake * 2, tile.getWidth(), tile.getHeight());
            }
        }

        shapeRenderer.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        scoreFont.draw(spriteBatch, String.valueOf(score), 100, height);
        if (timer > 0) timerFont.draw(spriteBatch, String.valueOf(timer), width - 100, height);
        spriteBatch.end();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public Controls getControls() {
        return controls;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public int getScore() {
        return score;
    }

    private void addFood() {
        Food newFood;
        isFoodExist = false;
        while (!isFoodExist) {
            newFood = new Food(xBoardSize, yBoardSize,null,true);
            if (!mFoods.contains(newFood, false)) {
                mFoods.add(newFood);
                isFoodExist = true;
            }
        }
    }
}


