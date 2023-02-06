package com.libgdx.roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class libGDXRoguelike extends ApplicationAdapter implements InputProcessor {
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    Viewport viewport;
    final int SCREEN_WIDTH = 720;
    final int SCREEN_HEIGHT = 400;

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(SCREEN_WIDTH, SCREEN_HEIGHT);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
//        camera.viewportHeight = screenHeight;
//        camera.viewportWidth = screenWidth;
//        camera.update();
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT,camera);

        tiledMap = new TmxMapLoader().load("roguelike_town.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Gdx.input.setInputProcessor(this);
        //camera.translate(-0.5f*32*w,-0.5f*32*h);

        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);

        float transX = mapWidth * 8 - SCREEN_WIDTH * 0.5f;
        float transY = mapHeight * 8 - SCREEN_HEIGHT * 0.5f;
        System.out.println(mapWidth + "#" + mapHeight + " transX ■ " + transX + " transY ■ " + transY);
        camera.translate(transX, transY);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0.25f, 0.5f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        //System.out.println(camera.position);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT)
            camera.translate(-32, 0);
        if (keycode == Input.Keys.RIGHT)
            camera.translate(32, 0);
        if (keycode == Input.Keys.UP)
            camera.translate(0, 32);
        if (keycode == Input.Keys.DOWN)
            camera.translate(0, -32);
        if (keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        if (keycode == Input.Keys.NUM_2)
            tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}