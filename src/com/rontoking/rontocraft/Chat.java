package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class Chat {
    public static boolean IS_CHAT_OPEN = false;
    public static String chatMsg = "";
    private static Array<String> TAB_HISTORY = new Array<String>();
    private static int TAB_INDEX = 0;
    public static Array<ChatMessage> chatArray = new Array<ChatMessage>();

    private static Color CHAT_BG_COLOR;
    private static Color CHAT_SINGLE_COLOR = Color.ORANGE;
    private static Color CHAT_MSG_COLOR = Color.WHITE;
    private static int CHAT_Y;
    private static int CHAT_HEIGHT;
    private static int MAX_CHAT_MSG_NUM = 8;

    public static Color SYSTEM_COLOR = Color.GOLD;
    public static String SYSTEM_NAME = "System";

    public static void load(){
        CHAT_Y = 10;
        CHAT_HEIGHT = 50;

        CHAT_BG_COLOR = Color.NAVY;
        CHAT_BG_COLOR.a = 0.8f;
    }

    public static void update(){
        if(Gdx.input.isCursorCatched()) {
            updateTime();
            updateLimit();
        }
    }

    private static void updateTime(){
        for(int i = 0; i < chatArray.size; i++) {
            if(chatArray.get(i).secondsLeft > 0) {
                chatArray.get(i).secondsLeft -= Main.deltaTime();
                return;
            }
        }
    }

    private static void updateLimit(){
        if(chatArray.size > MAX_CHAT_MSG_NUM){
            chatArray.removeIndex(0);
        }
    }

    public static void reset(){
        IS_CHAT_OPEN = false;
        chatMsg = "";
        chatArray.clear();
    }

    public static void startChatMSG(){
        IS_CHAT_OPEN = true;
        chatMsg = "";
    }

    public static void sendChatMSG() {
        IS_CHAT_OPEN = false;
        if(!chatMsg.equals("")) {
            if(chatMsg.length() > 64)
                chatMsg = chatMsg.substring(0, 64);
            TAB_HISTORY.insert(0, chatMsg);
            TAB_INDEX = 0;
            if(!Commands.tryParse(chatMsg)) {
                addToChat(new ChatMessage("Player", CHAT_SINGLE_COLOR, chatMsg));
            }
        }
    }

    public static void addToChat(ChatMessage msg){
        chatArray.add(msg);
        AudioManager.chatSound.play();
    }

    public static void type(Character character){
        if(Gdx.input.isCursorCatched()) {
            if (IS_CHAT_OPEN) {
                if ((Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) || Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && Gdx.input.isKeyJustPressed(Input.Keys.V))) {
                    try {
                        chatMsg += ((String) Toolkit.getDefaultToolkit()
                                .getSystemClipboard().getData(DataFlavor.stringFlavor)).replace("\n", "");
                    } catch (UnsupportedFlavorException e) {
                        //e.printStackTrace();
                    } catch (IOException e) {
                        //e.printStackTrace();
                    }
                } else if (!Character.isISOControl(character)) {
                    chatMsg += character;
                } else if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
                    if (chatMsg.length() > 0)
                        chatMsg = chatMsg.substring(0, chatMsg.length() - 1);
                } else if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
                    if(TAB_HISTORY.size > 0){
                        chatMsg = TAB_HISTORY.get(TAB_INDEX);
                        TAB_INDEX++;
                        if(TAB_INDEX > TAB_HISTORY.size - 1)
                            TAB_INDEX = 0;
                    }
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    sendChatMSG();
                }
            } else {
                if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                    startChatMSG();
                }
                else if (Gdx.input.isKeyJustPressed(Input.Keys.SLASH)) {
                    startChatMSG();
                    chatMsg = "/";
                }
            }
        }
    }

    public static void addSystemMsg(String msg){
        addToChat(new ChatMessage(SYSTEM_NAME, SYSTEM_COLOR, " " + msg));
    }

    public static void draw(){
        if(IS_CHAT_OPEN){
            Renderer.spriteBatch.setColor(CHAT_BG_COLOR);
            Renderer.spriteBatch.draw(TextureManager.pixelTexture, 0, CHAT_Y, Gdx.graphics.getWidth(), CHAT_HEIGHT);
            Renderer.spriteBatch.setColor(Color.WHITE);
            Renderer.drawTextCenteredY(chatMsg + "|", TextureManager.chatFont, 10, CHAT_Y + CHAT_HEIGHT / 2, CHAT_MSG_COLOR);
            drawChat(CHAT_Y + CHAT_HEIGHT + 10);
        }else {
            drawChat(CHAT_Y + 10);
        }
    }

    private static void drawChat(int y){
        for(int i = 0; i < chatArray.size && i < MAX_CHAT_MSG_NUM; i++)
            if(IS_CHAT_OPEN || chatArray.get(i).secondsLeft > 0)
                drawMSG(chatArray.get(i), i, y);
    }

    private static void drawMSG(ChatMessage msg, int index, int y){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < chatArray.size - index - 1; i++)
            stringBuilder.append("\n");
        Renderer.drawText(msg.senderSpaces + msg.text + stringBuilder.toString(), TextureManager.chatFont, 10, y, CHAT_MSG_COLOR);
        Renderer.drawText(msg.sender + ": " + stringBuilder.toString(), TextureManager.chatFont, 10, y, msg.senderColor);
    }
}
