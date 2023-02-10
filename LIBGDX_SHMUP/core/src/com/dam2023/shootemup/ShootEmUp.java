package com.dam2023.shootemup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import org.w3c.dom.Text;

import java.util.Random;

public class ShootEmUp extends Game {

    GameScreen gameScreen;
    Texture test;
    SpriteBatch batch;

    public static Random random = new Random();

    @Override
    public void create() {

        test = new Texture("world.png");

        gameScreen = new GameScreen();
        setScreen(gameScreen);
        batch = new SpriteBatch();
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }

    //@Override
    public void render() {

        super.render();

//        batch.begin();
//        batch.draw(test, 0, 0);
//        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }
}
