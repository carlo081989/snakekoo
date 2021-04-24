package com.crowd.snakekoo.archive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.crowd.snakekoo.game.SnakeGame;

public class ThreeDimensionScreen implements Screen {

    SnakeGame snakeGame;
    PerspectiveCamera camera;
    Environment environment;
    ModelBatch batch;
    Model model;
    ModelInstance modelInstance;

    public ThreeDimensionScreen (SnakeGame snakeGame) {
        this.snakeGame = snakeGame;
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10F, 10F, 10F);
        camera.lookAt(0,0,0);
        camera.near = 1F;
        camera.far = 300F;
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4F, 0.4F, 0.4F, 1F));
        environment.add(new DirectionalLight().set(0.8F, 0.8F, 0.8F, -1F, -0.8F, -0.2F));
        batch = new ModelBatch();
        ModelBuilder builder = new ModelBuilder();
        model = builder.createBox(4F, 4F, 4F,
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        modelInstance = new ModelInstance(model);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glViewport(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();
        batch.begin(camera);
        batch.render(modelInstance, environment);
        batch.end();
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
        model.dispose();
    }
}
