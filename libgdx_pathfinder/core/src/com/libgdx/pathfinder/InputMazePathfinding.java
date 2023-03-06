package com.libgdx.pathfinder;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class InputMazePathfinding implements InputProcessor {

    MyTiledMap myTiledMap;

    public InputMazePathfinding(MyTiledMap tiledMap) {
        myTiledMap = tiledMap;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchDown ***** " + screenX + "/" + screenY + " b:" + button);

        myTiledMap.mouseClicked(screenX, screenY, button);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        System.out.println("touchUp ***** " + screenX + "/" + screenY + " b:" + button);
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
