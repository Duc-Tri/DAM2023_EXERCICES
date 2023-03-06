package com.libgdx.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class PathFinder extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    TempMaze myMaze;

    @Override
    public void create() {
        batch = new SpriteBatch();
        myMaze = new TempMaze();

//		img = new Texture("tiny_16x16.png");
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        batch.begin();
//		batch.draw(img, 0, 0);
        myMaze.render(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
//        img.dispose();
    }
}
