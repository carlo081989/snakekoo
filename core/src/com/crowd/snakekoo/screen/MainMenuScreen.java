package com.crowd.snakekoo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.crowd.snakekoo.game.SnakeGame;
import com.crowd.snakekoo.helper.ButtonHelper;
import com.crowd.snakekoo.helper.FontHelper;
import com.crowd.snakekoo.helper.SliderHelper;
import com.crowd.snakekoo.object.Food;

public class MainMenuScreen extends ScreenAdapter {

    SnakeGame snakeGame;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    SpriteBatch spriteBatch;
    Texture playSheet;

    Stage stage;
    TextButton playButton;
    TextButton helpButton;
    TextButton backButton;

    private Array<Food> mFoods;

    private int xBoardSize; //How many squares in the board
    private int yBoardSize;
    private int yOffset; //How high the board is off the bottom
    private boolean isButtonPressed = false;
    private boolean justChecked = false;
    private int food;
    private float speed;

    public MainMenuScreen (SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        food = snakeGame.getPreference(int.class,"food") == null ? 1 : (int) snakeGame.getPreference(int.class, "food");
        speed = snakeGame.getPreference(float.class, "speed") == null ? 1 : (float) snakeGame.getPreference(float.class, "speed");
    }

    @Override
    public void show() {
        xBoardSize = (int) Math.round(Gdx.graphics.getWidth() / (Gdx.graphics.getWidth() * 0.05));
        yOffset = (int) Math.round(Gdx.graphics.getHeight() * 0.70);
        yBoardSize = (int) Math.round((Gdx.graphics.getHeight() - yOffset) / (Gdx.graphics.getHeight() * 0.025));
        mFoods = new Array<>();

        for (int x = 0; x <= MathUtils.random(15); x++) {
            mFoods.add(new Food(xBoardSize, yBoardSize,null,true));
        }

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        playButton = ButtonHelper.getButton("", "play.pack",
                "play_up", "play_down", "pause");
        playButton.addListener(new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                System.out.println("Button Pressed");
                if (!justChecked) isButtonPressed = true;
                else justChecked = false;
            }
        });
        helpButton = ButtonHelper.getButton("", "help.pack",
                "question", "light-bulb", null);
        helpButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                snakeGame.setScreen(snakeGame.helpScreen);
            }
        });
        backButton = ButtonHelper.getButton("", "back.pack",
                "back_up", "back_down", null);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                snakeGame.gameScreen = new GameScreen(snakeGame, food, speed);
                snakeGame.setScreen(snakeGame.gameScreen);
            }
        });
        if (snakeGame.gameScreen != null && snakeGame.gameScreen.gameState.isPaused()){
            justChecked = true;
            playButton.setChecked(true);
        }

        final Slider speedSlider = SliderHelper.getSlider(1, 10, 1, false);
        final Slider foodSlider = SliderHelper.getSlider(1, 100, 1, false);

        speedSlider.setValue(speed);
        foodSlider.setValue(food);

        speedSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                speed = speedSlider.getValue();
                snakeGame.setPreferences("speed", speed);
            }
        });

        foodSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                food = (int) foodSlider.getValue();
                snakeGame.setPreferences("food", food);
            }
        });

        Table table = new Table();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontHelper.getFont(50, Color.WHITE, false);
        Label speedLabel = new Label("speed", labelStyle);
        Label foodLabel = new Label("food", labelStyle);
        Label highLabel = new Label(Integer.toString(
                snakeGame.getPreference(int.class, "highscore") == null ? 0 :
                        (int) snakeGame.getPreference(int.class, "highscore")), labelStyle);
        table.setFillParent(true);
        table.setWidth(Gdx.graphics.getWidth());
        table.columnDefaults(0).expandX().left().uniformX();
        table.columnDefaults(1).expandX().center().uniformX();
        table.columnDefaults(2).expandX().right().uniformX();
        table.add(speedLabel).left().uniformX().pad(0,200,0,200).colspan(3);
        table.row();
        table.add(speedSlider).fillX().uniformX().pad(0,200,200,200).colspan(3);
        table.row();
        table.add(foodLabel).left().uniformX().pad(0,200,0,200).colspan(3);
        table.row();
        table.add(foodSlider).fillX().uniformX().pad(0,200,200,200).colspan(3);
        table.row();
        table.add(helpButton).right();
        table.add(playButton).center();
        table.add(backButton).left();
        backButton.setVisible(snakeGame.gameScreen != null && snakeGame.gameScreen.gameState.isPaused());
        table.row();
        table.add(highLabel).center().uniformX().pad(200,200,0,200).colspan(3);
        table.setPosition(0, -(stage.getHeight()/6));
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0F, 0F, 0F, 0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        update();
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0+5, yOffset+5, Gdx.graphics.getWidth()-5*2, (Gdx.graphics.getHeight()-yOffset)-5*2);

        float xScaleSnake = Gdx.graphics.getWidth()/xBoardSize; //width of one board square
        float yScaleSnake = (Gdx.graphics.getHeight()-yOffset)/yBoardSize;

        for (Food food : mFoods) {
            shapeRenderer.setColor(food.getColor());
            shapeRenderer.circle(food.getX() * xScaleSnake, food.getY()*yScaleSnake + yOffset, xScaleSnake/2);
        }

        shapeRenderer.end();

        stage.draw();
    }

    public void update() {
        if (isButtonPressed) {
            isButtonPressed = false;
            if (snakeGame.gameScreen != null && snakeGame.gameScreen.gameState.isPaused()) {
                snakeGame.gameScreen.resume();
            } else {
                snakeGame.gameScreen = new GameScreen(snakeGame, food, speed);
                snakeGame.setScreen(snakeGame.gameScreen);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        spriteBatch.dispose();
        playSheet.dispose();
        stage.dispose();
    }
}
