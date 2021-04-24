package com.crowd.snakekoo.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Json;
import com.crowd.snakekoo.screen.GameScreen;
import com.crowd.snakekoo.screen.HelpScreen;
import com.crowd.snakekoo.screen.MainMenuScreen;

import java.util.HashMap;
import java.util.Map;

public class SnakeGame extends Game {

    public MainMenuScreen mainMenuScreen;
    public GameScreen gameScreen;
    public HelpScreen helpScreen;
    public Preferences prefs;
    Json json;

    @Override
    public void create() {
        json = new Json();
        prefs = Gdx.app.getPreferences("data");
        mainMenuScreen = new MainMenuScreen(this); //color switching
        helpScreen = new HelpScreen(this);
        this.setScreen(mainMenuScreen);
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose () {

    }

    @Override
    public void setScreen (Screen screen) { super.setScreen(screen); }

    public void setPreferences(String key, Object object) {
        removePreference(key);
        prefs.putString(key, json.toJson(object));
        prefs.flush();
    }

    public Object getPreference(Class clazz, String key) {
        return prefs != null && prefs.getString(key) != null ? json.fromJson(clazz, prefs.getString(key)) : null;
    }

    public void removePreference(String key) {
        if (prefs != null)
            prefs.remove(key);
    }

}
