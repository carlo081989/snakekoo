package com.crowd.snakekoo.archive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.crowd.snakekoo.game.SnakeGame;

import java.util.Random;

public class GamePlayScreen implements Screen {

    SnakeGame snakeGame;
    OrthographicCamera camera;
    SpriteBatch spriteBatch;
    ShapeRenderer shapeRenderer;
    Sound effect;
    Vector3 touch;
    BitmapFont font;
    Preferences data;

    int x = (1440 / 2) - 64;
    int y = (2560 / 2) - 64;
    int score = 0;

    TextureRegion[] playFrames;
    TextureRegion currentFrame;
    Animation playAnimation;
    float stateTime;

    public GamePlayScreen(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(true, 1440, 2560);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        effect = Gdx.audio.newSound(Gdx.files.internal("button-3.wav"));
        touch = new Vector3();
        data = Gdx.app.getPreferences("Our Data");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SEASRN__.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 100;
        parameter.color = Color.RED;
        parameter.flip = true;
        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        score = data.getInteger("score", 0);

//        playSheet = new Texture(Gdx.files.internal("c07d8f3be977218f8ba2bcd5e74525ee.png"));
//        TextureRegion[][] temp = TextureRegion.split(playSheet, 980, 714);
//        playFrames = new TextureRegion[6];
//        int index = 0;
//        for (int i = 0 ; i < 2 ; i++) {
//            for (int j = 0 ; j < 3 ; j++) {
//                temp[i][j].flip(false, true);
//                playFrames[index++] = temp[i][j];
//            }
//        }
//        playAnimation = new Animation(0.5F, playFrames);
//        stateTime = 0F;
    }

    @Override
    public void show() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Random random = new Random();
                x = random.nextInt(1440);
                y = random.nextInt(2560);
            }
        }, 0, 2);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(x, y,128,128);
        shapeRenderer.end();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        font.draw(spriteBatch, String.valueOf(score), 0, 0);
        spriteBatch.end();

//        stateTime += delta;
//        currentFrame = (TextureRegion) playAnimation.getKeyFrame(stateTime, true);
//        spriteBatch.begin();
//        spriteBatch.draw(currentFrame, 0, 400);
//        spriteBatch.end();
    }

    public void update(){
        if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer)
                && Gdx.input.getRotation() == 0 && isTouched(x, y,128,128)) {
            score++;
            data.putInteger("score", score);
            data.flush();
            effect.play();
            Gdx.input.vibrate(500);
            //Gdx.input.vibrate(new long[]{500, 0, 200, 0, 100}, 5);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isTouched (int x, int y, int width, int height) {
        if (Gdx.input.isTouched()) {
            touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touch);
            if (touch.x >= x && touch.x <= x + width
                    && touch.y >= y && touch.y <= y + height) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        effect.dispose();

    }
}
