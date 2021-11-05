package com.rontoking.rontocraft;

import com.badlogic.gdx.graphics.Color;

public class ChatMessage {
    public String sender, text, senderSpaces;
    public Color senderColor;
    public float secondsLeft;

    public ChatMessage(String sender, Color senderColor, String text){
        this.sender = sender;
        this.senderSpaces = "  ";
        for(int i = 0; i < sender.length(); i++)
            senderSpaces += "  ";
        this.senderColor = senderColor;
        this.text = text;
        this.secondsLeft = 2.5f * text.trim().replaceAll(" +", " ").split(" ").length;
    }
}
