package com.crowd.snakekoo.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.awt.Font;

public class ButtonHelper {

    public static TextButton getButton (String text) {
        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("test-me.pack")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable("test01");
        textButtonStyle.down = skin.getDrawable("test02 (non POT)");
        textButtonStyle.checked = skin.getDrawable("test03 (multi shapes)");
        return new TextButton(text, textButtonStyle);
    }

    public static TextButton getButton (String text, String pack, String up, String down, String checked) {
        Skin skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal(pack)));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.up = skin.getDrawable(up);
        textButtonStyle.down = skin.getDrawable(down);
        if (checked != null)
            textButtonStyle.checked = skin.getDrawable(checked);
        return new TextButton(text, textButtonStyle);
    }
}
