package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

public class TextureManager {

    public static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("blocks/blocks.atlas"));
    public static final Array<TextureAtlas.AtlasRegion> regions = new Array<TextureAtlas.AtlasRegion>();

    public static final Texture crosshairTexture = new Texture(Gdx.files.internal("gui/crosshair.png"));
    public static final Texture selectedBlockTexture = new Texture(Gdx.files.internal("gui/selected block.png"));
    public static final Texture pixelTexture = new Texture(Gdx.files.internal("gui/pixel.png"));

    public static final BitmapFont defaultFont = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
    public static BitmapFont caveStory, chatFont;
    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    public static void loadTextures(){
        loadBlockTextures();
        loadFonts();
    }

    public static void loadBlockTextures(){
        for(Block.Type t : Block.Type.values()){
            if(t.hasDifferentTop)
                regions.add(atlas.findRegion(t.name + " side"));
            else
                regions.add(atlas.findRegion(t.name));
        }
    }

    public static void loadFonts(){
        defaultFont.setColor(Color.WHITE);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cave story.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.color = Color.WHITE;
        caveStory = generator.generateFont(parameter);

        parameter.size = 45;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        chatFont = generator.generateFont(parameter);

        generator.dispose();
    }

    public static void dispose(){
        atlas.dispose();
        crosshairTexture.dispose();
        selectedBlockTexture.dispose();
        pixelTexture.dispose();
        defaultFont.dispose();
        caveStory.dispose();
        chatFont.dispose();
    }
}
