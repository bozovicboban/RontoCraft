package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.IntIntMap;

/**
 * Created by Core 2 Duo on 16.8.2016.
 */
public class InputManager extends InputAdapter {
    public static final IntIntMap keys = new IntIntMap();

    public static int leftKey = Input.Keys.A;
    public static int rightKey = Input.Keys.D;
    public static int forwardKey = Input.Keys.W;
    public static int backwardKey = Input.Keys.S;
    public static int jumpKey = Input.Keys.SPACE;
    public static int sprintKey = Input.Keys.CONTROL_LEFT;
    public static int pauseKey = Input.Keys.ESCAPE;
    public static int vsyncKey = Input.Keys.T;
    public static int toggleTextKey = Input.Keys.R;
    public static int fullscreenKey = Input.Keys.F;
    public static int selectionRightKey = Input.Keys.E;
    public static int selectionLeftKey = Input.Keys.Q;

    @Override
    public boolean keyDown (int keycode) {
        InputManager.keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp (int keycode) {
        InputManager.keys.remove(keycode, keycode);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY){
        if (!Chat.IS_CHAT_OPEN && Main.gameState.equals("game")) {
            CameraManager.updateCamera(new Point2(screenX, screenY));
        }

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        if (!Chat.IS_CHAT_OPEN && Main.gameState.equals("game")) {
            if(amount > 0){
                if(GameInput.selectedBlock == Block.firstID)
                    GameInput.selectedBlock = Block.lastID;
                else
                    GameInput.selectedBlock--;
            }else{
                if (GameInput.selectedBlock == Block.lastID)
                    GameInput.selectedBlock = Block.firstID;
                else
                    GameInput.selectedBlock++;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer){
        if (!Chat.IS_CHAT_OPEN && Main.gameState.equals("game")) {
            CameraManager.updateCamera(new Point2(screenX, screenY));
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if(Main.gameState.equals("game"))
            Chat.type(character);
        return false;
    }

    public static void update(){
        toggleFullscreen();
        togglePause();
        if (Main.gameState.equals("game")) {
            GameInput.update();
        }
        else if(Main.gameState.equals("menu")){
            MenuInput.update();
        }
    }

    public static void togglePause(){
        if(keys.containsKey(pauseKey)){
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            keys.remove(pauseKey, pauseKey);
            if(Main.gameState.equals("menu")){
                Main.gameState = "game";
                CameraManager.prevMousePos.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                Gdx.input.setCursorCatched(true);
            }else{
                if(Chat.IS_CHAT_OPEN){
                    Chat.IS_CHAT_OPEN = false;
                }else {
                    Main.gameState = "menu";
                    Gdx.input.setCursorCatched(false);
                }
            }
        }
    }

    public static void toggleFullscreen(){
        if(!Chat.IS_CHAT_OPEN && InputManager.keys.containsKey(InputManager.fullscreenKey)){
            InputManager.keys.remove(InputManager.fullscreenKey, 0);
            if(Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(Main.windowWidth, Main.windowHeight);
            }
            else {
                Main.windowWidth = Gdx.graphics.getWidth();
                Main.windowHeight = Gdx.graphics.getHeight();
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }
    }
}
