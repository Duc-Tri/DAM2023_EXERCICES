package com.dam2023.shootemup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TextureRegion[] backgrounds;
    private float backgroundHeight; // height of background in world units
    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion, enemyShipTextureRegion, enemyShieldTextureRegion, playerLaserTextureRegion, enemyLaserTextureRegion;

    private float[] backgroundOffsets = {0, 0, 0};//, 0};
    private float backgroundMaxScrollingSpeed;

    //world parameters
    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;

    // game objects
    private PlayerShip playerShip;
    private EnemyShip enemyShip;

    private LinkedList<Laser> playerLaserList;
    private LinkedList<Laser> enemyLaserList;

    GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //set up texture atlas
        textureAtlas = new TextureAtlas("images.atlas");

        // /!\ SUR SAMSUNG SM-T560, NE PAS DEPASSER 4090 PIXELS DE LARGEUR / HAUTEUR

        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");

        backgroundHeight = WORLD_HEIGHT * 2;
        backgroundMaxScrollingSpeed = (float) (WORLD_HEIGHT) / 4;

        //initialize txtures regions
        playerShipTextureRegion = textureAtlas.findRegion("playerShip2_blue");
        enemyShipTextureRegion = textureAtlas.findRegion("enemyRed3");
        playerLaserTextureRegion = textureAtlas.findRegion("laserBlue03");
        enemyLaserTextureRegion = textureAtlas.findRegion("laserRed03");
        playerShieldTextureRegion = textureAtlas.findRegion("shield2");
        enemyShieldTextureRegion = textureAtlas.findRegion("shield1");
        enemyShieldTextureRegion.flip(false, true);

        //set up gameobjects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                10, 10,
                36, 6,
                .4f, 4, 45, .5f,
                playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShip = new EnemyShip(WORLD_WIDTH / 2, WORLD_HEIGHT * 3 / 4,
                10, 10,
                2, 5,
                .3f, 5, 50, .8f,
                enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion);

        playerLaserList = new LinkedList<>();
        enemyLaserList = new LinkedList<>();

        batch = new SpriteBatch();
    }

    Texture test = new Texture("world.png");

    //    @Override
    public void render1(float deltaTime) {
        Gdx.gl.glClearColor(0.9f, 0.4f, 0.7f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(backgrounds[0], 0, 0);
        batch.draw(backgrounds[1], 100, 100);
        batch.draw(backgrounds[2], 200, 200);
        batch.draw(backgrounds[3], 300, 300);

        batch.draw(test, 400, 400);

        //renderBackground(deltaTime);

        batch.end();
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        detectInput(deltaTime);

        playerShip.update(deltaTime);
        enemyShip.update(deltaTime);

        //scrolling background
        renderBackground(deltaTime);

        //enemy ships
        enemyShip.draw(batch);

        //player ship
        playerShip.draw(batch);

        // draw lasers ========================================================
        renderLasers(deltaTime);

        //detect collisions
        detectCollisions();

        //explosions ==========================================================
        renderExplosions(deltaTime);

        batch.end();
    }

    private void detectInput(float deltaTime) {
        //keyboard

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            playerShip.translate(Math.min(playerShip.movementSpeed * deltaTime, rightLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            playerShip.translate(0f, Math.min(playerShip.movementSpeed * deltaTime, upLimit));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            playerShip.translate(Math.max(-playerShip.movementSpeed * deltaTime, leftLimit), 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            playerShip.translate(0f, Math.max(-playerShip.movementSpeed * deltaTime, downLimit));
        }


        //touch & mouse

    }

    private void detectCollisions() {
        //for each player laser check if it intersects enemy ship
        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (enemyShip.intersects(laser.boundingBox)) {
                enemyShip.hit(laser);
                iterator.remove();
            }
        }

        //for each enemy laser check if it intersects player ship
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (playerShip.intersects(laser.boundingBox)) {
                playerShip.hit(laser);
                iterator.remove();
            }
        }
    }

    private void renderExplosions(float deltaTime) {
    }

    private void renderLasers(float deltaTime) {
        // create new lasers ==================================================
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            for (Laser laser : lasers) {
                playerLaserList.add(laser);
            }
        }
        if (enemyShip.canFireLaser()) {
            Laser[] lasers = enemyShip.fireLasers();
            for (Laser laser : lasers) {
                enemyLaserList.add(laser);
            }
        }

        // remove old lasers ==================================================
        ListIterator<Laser> iterator = playerLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT)
                iterator.remove();
        }
        iterator = enemyLaserList.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height < 0)
                iterator.remove();
        }
    }

    private void renderBackground(float deltaTime) {

        for (int layer = 0; layer < backgroundOffsets.length; layer++) {
            backgroundOffsets[layer] += deltaTime * backgroundMaxScrollingSpeed / Math.pow(2, backgroundOffsets.length - layer);

            if (backgroundOffsets[layer] > WORLD_HEIGHT) {
                backgroundOffsets[layer] = 0;
            }
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(backgrounds[layer], 0, -backgroundOffsets[layer] + WORLD_HEIGHT, WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }


    @Override
    public void show() {
    }

}
