package com.crowd.snakekoo.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.crowd.snakekoo.game.SnakeGame;
import com.crowd.snakekoo.helper.FontHelper;

public class HelpScreen extends ScreenAdapter {

    SnakeGame snakeGame;
    Stage stage;

    public HelpScreen(SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void show () {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = FontHelper.getFont(35, Color.WHITE, false);
        Label headerLabel = new Label("Instruction: \n", labelStyle);
        Label contentLabel = new Label("Guide the snake by swiping up, left, right, or down on " +
                "the play area (black background). \n \nChange the color of the snake by matching at " +
                "least three circles of the same color in the work area (white background). You can do this " +
                "by tapping the matching circles, then tap on the play area to apply the color to the snake. " +
                "The color countdown timer will start (upper right). \nThe color of the snake determines what " +
                "food it can eat. \nThe number of circles matched determines how long the snake will stay on " +
                "that color (at most two seconds per circle). \n \nGame will be over if: \n1. Snake hits its " +
                "own body \n2. Swallowed food of different color \n3. Score has reached a hundred below zero \n" +
                "4. No more food left \n \nYou can adjust the speed and food density on the menu screen. " +
                "\n \nScoring: \nYour score is based on the speed multiplied by the number of foods present " +
                "on the play area. It will continuously decrease when color countdown timer is off.", labelStyle);
        Label footerLabel = new Label("\n \n \n Bitin'! v1.0.0", labelStyle);
        headerLabel.setAlignment(Align.left);
        contentLabel.setAlignment(Align.left);
        footerLabel.setAlignment(Align.left);
        contentLabel.setWrap(true);
        footerLabel.setWrap(true);
        contentLabel.setWidth(Gdx.graphics.getWidth());

        Table table = new Table();
        table.setFillParent(true);
        table.columnDefaults(0).expandX().left().uniformX();
        table.columnDefaults(0).expandX().right().uniformX();
        table.add(headerLabel).left().uniformX();
        table.row();
        table.add(contentLabel).left().uniformX().width(Gdx.graphics.getWidth());
        table.row();
        table.add(footerLabel).left().uniformX();
        table.row();

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0F, 0F, 0F, 0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
            snakeGame.setScreen(snakeGame.mainMenuScreen);
        }
        stage.draw();
    }

}
