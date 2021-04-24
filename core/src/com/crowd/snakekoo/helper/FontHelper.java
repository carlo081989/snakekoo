package com.crowd.snakekoo.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontHelper {

    public static BitmapFont getFont (int size, Color color, boolean flip) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("SEASRN__.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter= new FreeTypeFontGenerator.FreeTypeFontParameter();
        BitmapFont font;
        parameter.size = size;
        parameter.color = color;
        parameter.flip = flip;
        font = generator.generateFont(parameter);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        generator.dispose();
        return font;
    }

}
