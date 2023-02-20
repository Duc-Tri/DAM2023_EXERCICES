package com.libgdx.pathfinder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class Maze {

    public static final int MAZE_WIDTH = 40;
    public static final int MAZE_HEIGHT = 30;


    public static final int TILE_SIZE = 16;
    private final static Texture tiles = new Texture("tiny_16x16.png");

    private final static TextureRegion trWall = new TextureRegion(tiles, 80, 48, TILE_SIZE, TILE_SIZE);
    private final static TextureRegion trFloor = new TextureRegion(tiles, 16, 64, TILE_SIZE, TILE_SIZE);
    private final static TextureRegion trStart = new TextureRegion(tiles, 16, 112, TILE_SIZE, TILE_SIZE);
    private final static TextureRegion trFinish = new TextureRegion(tiles, 336, 146, TILE_SIZE, TILE_SIZE);
    private final static TextureRegion trPath = new TextureRegion(tiles, 0, 80, TILE_SIZE, TILE_SIZE);

    public static final int START = 2;
    public static final int FINISH = 9;
    public static final int WALL = 1;
    public static final int FLOOR = 0;

    public static final Map<Integer, TextureRegion> textureRegions = new HashMap<>();//Map.of(START, trStart, FINISH, trFinish, FLOOR, trFloor, WALL, trWall);
    private static final int SCREEN_XOFFSET = 100;
    private static final int SCREEN_YOFFSET = 100;


    public int[][] array = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 9, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}

    };

    public Maze() {
//Map.of(START, trStart, FINISH, trFinish, FLOOR, trFloor, WALL, trWall);
        textureRegions.put(START, trStart);
        textureRegions.put(FINISH, trFinish);
        textureRegions.put(FLOOR, trFloor);
        textureRegions.put(WALL, trWall);
    }

    public void render(SpriteBatch batch) {
        for (int y = array.length - 1; y >= 0; y--) {
            for (int x = array[y].length - 1; x >= 0; x--) {
                batch.draw(NumToTile(array[y][x]), x * TILE_SIZE+SCREEN_XOFFSET,
                        Gdx.graphics.getHeight() - (y + 1) * TILE_SIZE-SCREEN_YOFFSET);
            }
        }
    }

    private TextureRegion NumToTile(int i) {
        return textureRegions.get(i);
    }
}
