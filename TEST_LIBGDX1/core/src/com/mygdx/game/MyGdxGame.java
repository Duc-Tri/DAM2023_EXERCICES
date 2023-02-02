package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {
    private Texture background, texture;
    private SpriteBatch spriteBatch;
    int posX, posY, screenHeight;
    int textHalfWidth, textHalfHeight;

    @Override
    public void create() {
        texture = new Texture("world.png");
        background = new Texture("space-background.jpg");
        spriteBatch = new SpriteBatch();

        screenHeight = Gdx.graphics.getHeight();
        textHalfHeight = texture.getHeight() / 2;
        textHalfWidth = texture.getWidth() / 2;
        posX = Gdx.graphics.getWidth() / 2 - textHalfWidth;
        posY = screenHeight / 2 - textHalfHeight;
    }

    public void render() {
        //Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(background, 0, 0);
        spriteBatch.draw(texture, posX, posY);
        spriteBatch.end();

        if (Gdx.input.isTouched()) {
            posX = Gdx.input.getX() - textHalfWidth;
            posY = screenHeight - Gdx.input.getY() - textHalfHeight;

            //System.out.println(posX + " / " + posY);
        }
    }

    public void dispose() {
        texture.dispose();
        background.dispose();
        spriteBatch.dispose();
    }

}
