package com.crowd.snakekoo.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;

public class SliderHelper {

    public static Slider getSlider(float min, float max, float stepSize, boolean vertical) {
        PixmapPacker packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 2, true);
        packer.pack("background", new Pixmap(Gdx.files.internal("knob-background.png")));
        packer.pack("knob", new Pixmap(Gdx.files.internal("knob.png")));
        packer.pack("knob-before", new Pixmap(Gdx.files.internal("knob-before.png")));
        packer.pack("knob-after", new Pixmap(Gdx.files.internal("knob-after.png")));
        Skin sliderSkin = new Skin(packer.generateTextureAtlas(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear, false));
        packer.dispose();

        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = sliderSkin.getDrawable("background");
        sliderStyle.knob = sliderSkin.getDrawable("knob");
        sliderStyle.knobBefore = sliderSkin.getDrawable("knob-before");
        sliderStyle.knobAfter = sliderSkin.getDrawable("knob-after");
        sliderStyle.knob.setMinHeight(100f);

        return new Slider(min, max, stepSize, vertical, sliderStyle);
    }

}
