package com.crowd.snakekoo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.crowd.snakekoo.state.GameState;
import com.crowd.snakekoo.game.SnakeGame;

public class GameScreen implements Screen {

    private SnakeGame game;

    private int width = Gdx.graphics.getWidth();
    private int height = Gdx.graphics.getHeight();

    private OrthographicCamera camera = new OrthographicCamera(width, height);

    public Viewport viewport;
    public GameState gameState;

    public GameScreen(SnakeGame game, int food, float speed) {
        this.game = game;
        gameState = new GameState(width, height, food, 11 - (speed == 0 ? 1 : speed));
        camera.setToOrtho(false, width, height);
        viewport = new FitViewport(width, height, camera);
        viewport.apply();
    }

    @Override
    public void show() {
        gameState.getControls().setControls();
    }

    @Override
    public void render(float delta) {
        System.out.println("Render method was called.");
        camera.update();
        viewport.apply();

        gameState.update(delta, viewport);

        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameState.draw(width, height, camera);

        if (gameState.isGameOver()) {
            //game.setPreferences("saveState", false);
            if ((game.getPreference(int.class, "highscore") == null ? 0 :
                    (int) game.getPreference(int.class, "highscore")) < gameState.getScore())
                game.setPreferences("highscore",gameState.getScore());
            game.setScreen(game.mainMenuScreen);
        }

        if (gameState.isPaused()) {
            pause();
        }
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("Resize method was called.");
        viewport.update(width, height);
    }

    @Override
    public void pause() {
        System.out.println("Pause method was called.");
        game.setScreen(game.mainMenuScreen);
    }

    @Override
    public void resume() {
        System.out.println("Resume method was called.");
        game.gameScreen.gameState.setPaused(false);
        game.setScreen(game.gameScreen);
    }

    @Override
    public void hide() {
        System.out.println("Hide method was called.");
        if (!gameState.isGameOver()) {
            gameState.setPaused(true);
            //game.setPreferences("saveState", gameState);
            //gameState.saveState(game);
        }
    }

    @Override
    public void dispose() {
        System.out.println("Dispose method was called.");
    }
}

