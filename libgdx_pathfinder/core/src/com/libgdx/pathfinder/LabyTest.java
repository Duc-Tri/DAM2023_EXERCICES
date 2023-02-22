package com.libgdx.pathfinder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class LabyTest extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;

    TiledMap labyte;
    TiledMap holder;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;

    @Override
    public void create() {

        createHolderMap();

        camera = new OrthographicCamera();
        camera.setToOrtho(false,800,600);
        camera.translate(-300,-200);
        camera.update();

        batch = new SpriteBatch();

        labyte = new TmxMapLoader().load("BlocMap/Bloc1.tmx");

        tiledMapRenderer = new OrthogonalTiledMapRenderer(labyte);


//		img = new Texture("tiny_16x16.png");
    }

    private void createHolderMap() {
//        holder = new TiledMap();
//        holder.getLayers().get(0);
//
//        MapLayer layer =new MapLayer();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        batch.begin();
//		batch.draw(img, 0, 0);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
//        img.dispose();
    }
}
