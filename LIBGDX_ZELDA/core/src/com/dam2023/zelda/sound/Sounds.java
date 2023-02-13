package com.dam2023.zelda.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Aurelien on 22/12/2015.
 */
public class Sounds
{
    public static Sound heroHurt;
    public static Sound enemyHurt;
    public static Sound enemyDie;

    public static Sound swordSlash1;
    public static Sound swordSlash2;
    public static Sound swordSlash3;

    public static void registerSounds()
    {
        heroHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/hero_hurt.mp3"));
        enemyHurt = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_hurt.mp3"));
        enemyDie = Gdx.audio.newSound(Gdx.files.internal("sounds/enemy_die.mp3"));

        swordSlash1 = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_slash_1.mp3"));
        swordSlash2 = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_slash_2.mp3"));
        swordSlash3 = Gdx.audio.newSound(Gdx.files.internal("sounds/sword_slash_3.mp3"));
    }
}
