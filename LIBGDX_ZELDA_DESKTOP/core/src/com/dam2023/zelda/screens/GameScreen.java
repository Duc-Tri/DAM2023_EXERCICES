package com.dam2023.zelda.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.zelda.ZeldaGDX;
import com.dam2023.zelda.map.Chunk;
import com.dam2023.zelda.map.Map;
import com.dam2023.zelda.sound.Musics;
import com.dam2023.zelda.tiles.Tile;
import com.dam2023.zelda.world.World;
import com.mygdx.zelda.ZeldaGDX;

/**
 * L'écran de jeu. Il peut gérer les inputs qui lui sont propres à condition de le definir en tant
 * qu'InputProcessor de Gdx.input
 */
public class GameScreen implements Screen
{
    private final ZeldaGDX game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private float time;

    public static float TIME_STEP = 1/60f;
    public static int VELOCITY_ITERATIONS = 6;
    public static int POSITION_ITERATIONS = 2;

    public GameScreen(ZeldaGDX game)
    {
        this.game = game;
        this.batch = new SpriteBatch();
        this.time = 0;

        float w = 864;
        float h = 480;
        this.camera = new OrthographicCamera(w, h);
        float tw = Tile.TILE_SIZE * Chunk.CHUNK_TILE_SIZE * Map.MAP_CHUNK_SIZE;

        camera.position.set(w / 2f, h / 2f, 0);
        camera.zoom = 0.5f;

        camera.translate((tw - w) / 2, (tw - h) / 2);
        camera.update();
    }

    @Override
    public void show()
    {
        Musics.exterieur.setLooping(true);
        Musics.exterieur.play();
    }

    @Override
    public void render(float delta)
    {
        // Déplacer le héros
        World.getHero().update();
        // Positionner la caméra sur le héros
        camera.position.set(World.getHero().x * Tile.TILE_SIZE, World.getHero().y * Tile.TILE_SIZE, 0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        time += Gdx.graphics.getDeltaTime();

        // Dessiner
        batch.begin();
        World.getCurrentMap().draw(batch, time);
        World.getHero().draw(batch, time);
        batch.end();
    }

    private final float accumulator = 0;

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }

}
