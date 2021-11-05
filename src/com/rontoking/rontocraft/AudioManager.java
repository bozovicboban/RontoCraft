package com.rontoking.rontocraft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    public static Sound chatSound = Gdx.audio.newSound(Gdx.files.internal("audio/chat.ogg"));
    public static Sound blockSound = Gdx.audio.newSound(Gdx.files.internal("audio/block.ogg"));
    public static Music music = Gdx.audio.newMusic(Gdx.files.internal("audio/music.mp3"));

    public static void dispose(){
        chatSound.dispose();
        music.dispose();
    }
}
