package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;

public class Renderer {

    public static final SpriteBatch spriteBatch = new SpriteBatch();
    public static final ModelBatch modelBatch = new ModelBatch();

    public static boolean isChunkBeingBuilt = false;
    public static int showGUI = 0;
    private static GlyphLayout glyphLayout = new GlyphLayout();

    public static Array<Chunk> chunksToBuild = new Array<Chunk>();

    public static void render() {
        Gdx.gl.glClearColor(0.53f, 0.8f, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(CameraManager.camera);
        //modelBatch.getRenderContext().setCullFace(0);
        //modelBatch.getRenderContext().setBlending(false, 0, 0);
        //Gdx.gl.glDisable(Gdx.gl30.GL_BLEND);
        //modelBatch.render(ModelManager.floorInstance, World.environment);

        renderChunks();

        if (!GameInput.lookedAtBlock.equals(Point3.none) && showGUI <= 2) {
            modelBatch.render(ModelManager.selectedBlockInstance, World.environment);
        }
        modelBatch.render(ModelManager.skyInstance);
        modelBatch.end();

        spriteBatch.begin();
        drawGUI();
        spriteBatch.end();
    }

    public static void renderChunks(){
        for(int i = 0; i < CameraManager.frustumChunks.size; i++) {
            if (!isChunkBeingBuilt && CameraManager.frustumChunks.get(i).shouldBuild) {
                buildChunkModelCache(CameraManager.frustumChunks.get(i));
            }
            if (!CameraManager.frustumChunks.get(i).isBuilding && CameraManager.frustumChunks.get(i).isVisible && CameraManager.frustumChunks.get(i).numOfBlocks > 0)
                modelBatch.render(CameraManager.frustumChunks.get(i).modelCache, World.environment);
        }
    }

    private static void buildChunkModelCache(final Chunk chunk) {
        if (!chunk.isBuilding) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    isChunkBeingBuilt = true;
                    chunk.buildModelCache();
                }
            }).start();
        }
    }

    public static void drawGUI(){
        if(showGUI == 0) {
            drawGUIText("WASD - Move \nSpace - Jump \nControl - Sprint \nE / Q / Mouse Wheel / Number Keys - Change Selected Block \n\nEscape - Pause / Unpause \nF - Toggle Fullscreen \nT - Toggle VSync \nR - Switch GUI", 0);
        }
        if(showGUI == 1) {
            drawGUIText("FPS: " + Gdx.graphics.getFramesPerSecond(), 0);
            drawGUIText("Position: " + Utility.displayFloat(Player.position.x, 1) + ", " + Utility.displayFloat(Player.position.y, 1) + ", " + Utility.displayFloat(Player.position.z, 1), 40);
            drawGUIText("Chunks: " + World.chunkArray.size + " / " + Save.generatedChunks.size, 80);
            drawGUIText("Visible Chunks: " + CameraManager.frustumChunks.size, 120);
            drawGUIText("Blocks: " + World.loadedBlockNum + " / " + World.totalBlockNum, 160);
            drawGUIText("Selected Block: ", 200);
            drawGUIText("Looked At Block: " + Block.nameOf(World.getBlockAtPoint(GameInput.lookedAtBlock)), 240);
            //if(World.getChunkAtPoint(GameInput.lookedAtBlock) != null)
            //    drawGUIText("Chunk Block Num: " + World.getChunkAtPoint(GameInput.lookedAtBlock).numOfBlocks, 280);
            //else
            //    drawGUIText("Chunk Block Num: NO CHUNK", 280);
            spriteBatch.draw(TextureManager.regions.get(GameInput.selectedBlock - Block.firstID), 257, Main.height - 233, 32, 32);
        }
        else if(showGUI == 2){
            spriteBatch.draw(TextureManager.regions.get(GameInput.selectedBlock - Block.firstID), 0, Main.height - 64, 64, 64);
        }
        if(showGUI <= 3)
            spriteBatch.draw(TextureManager.crosshairTexture, Main.width / 2 - 9, Main.height / 2 - 7);
        Chat.draw();
    }

    public static void drawGUIText(String text, int y){
        TextureManager.caveStory.draw(spriteBatch, text, 5, Main.height - y - 5);
    }

    public static void drawText(String text, BitmapFont font, int x, int y, Color color) {
        glyphLayout.setText(font, text);
        font.setColor(color);
        font.draw(spriteBatch, text, x, y + glyphLayout.height);
    }

    public static void drawTextCenteredY(String text, BitmapFont font, int x, int y, Color color) {
        glyphLayout.setText(font, text);
        font.setColor(color);
        font.draw(spriteBatch, text, x, y + glyphLayout.height / 2);
    }

    public static void dispose(){
        modelBatch.dispose();
        spriteBatch.dispose();
    }
}
