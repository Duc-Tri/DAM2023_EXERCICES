package com.dam2023.zelda.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Aurelien on 14/01/2016.
 */
public class Animations {
    protected static final String PATH = "sprites/animations/";

    public static Animation monsterDeathAnimation;

    public static void registerAnimations() {
        monsterDeathAnimation = createAnimation("monster_death_animation", 0.1f, 32, 32, Animation.PlayMode.LOOP_REVERSED);
    }

    public static Animation createAnimation(String name, float frameTime, int regionWidth, int regionHeight, Animation.PlayMode mode) {
        Animation animation = new Animation(frameTime, TextureRegion.split(new Texture(Gdx.files.internal(PATH + name + ".png")), regionWidth, regionHeight)[0]);
        animation.setPlayMode(mode);
        return animation;
    }
}
