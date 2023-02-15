package com.dam2023.shootemup;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements Screen {


    //screen
    private Camera camera;
    private Viewport viewport;

    //graphics
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Texture explosionTexture;
    private TextureRegion[] backgrounds;
    private float backgroundHeight; // height of background in world units
    private TextureRegion playerShipTextureRegion, playerShieldTextureRegion, enemyShipTextureRegion, enemyShieldTextureRegion, playerLaserTextureRegion, enemyLaserTextureRegion;

    private float[] backgroundOffsets = {0, 0, 0};//, 0};
    private float backgroundMaxScrollingSpeed;
    private float timeBetweenEnemySpawns = 1f;
    private float enemySpawnTimer = 0;

    //world parameters
    private final float WORLD_WIDTH = 72;
    private final float WORLD_HEIGHT = 128;
    private final float TOUCH_MOVEMENT_THRESHOLD = 0.5f;

    // game objects
    private PlayerShip playerShip;
    private LinkedList<EnemyShip> enemyShips;

    private LinkedList<Laser> playerLasers;
    private LinkedList<Laser> enemyLasers;
    private LinkedList<Explosion> explosions;

    private int score = 0;

    // Head-Up display
    BitmapFont font;
    float HUDVerticalMargin, HUDLeftX, HUDRightX, HUDCentreX, HUDRow1Y, HUDRow2Y, HUDSectionWidth;


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

        explosionTexture = new Texture("explosion.png");
        //set up gameobjects
        playerShip = new PlayerShip(WORLD_WIDTH / 2, WORLD_HEIGHT / 4,
                10, 10,
                70, 6,
                .4f, 4, 45, .5f,
                playerShipTextureRegion, playerShieldTextureRegion, playerLaserTextureRegion);

        enemyShips = new LinkedList<>();
        playerLasers = new LinkedList<>();
        enemyLasers = new LinkedList<>();
        explosions = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD();
    }

    private void prepareHUD() {
        // create bitmap font ------------------------------------------------------------
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.5f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.5f);
        fontParameter.spaceX = 5;

        font = fontGenerator.generateFont(fontParameter);

        //scale font to fit world -----------------------------------------------------------------
        font.getData().setScale(0.07f); // ??????????

        //calculate HUD margins, etc.

        HUDVerticalMargin = font.getCapHeight() / 2;
        HUDLeftX = HUDVerticalMargin;
        HUDRightX = WORLD_WIDTH * 2 / 3 - HUDLeftX;
        HUDCentreX = WORLD_WIDTH / 3;
        HUDRow1Y = WORLD_HEIGHT - HUDVerticalMargin;
        HUDRow2Y = HUDRow1Y - HUDVerticalMargin - font.getCapHeight();
        HUDSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();

        //scrolling background
        renderBackground(deltaTime);

        detectInput(deltaTime);
        playerShip.update(deltaTime);

        spawnEnemyShips(deltaTime);

        //enemy ships
        ListIterator<EnemyShip> enemyShipsIterator = enemyShips.listIterator();
        while (enemyShipsIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipsIterator.next();
            moveEnemy(enemyShip, deltaTime);
            enemyShip.update(deltaTime);
            enemyShip.draw(batch);
        }

        //player ship
        playerShip.draw(batch);

        // draw lasers ========================================================
        renderLasers(deltaTime);

        //detect collisions
        detectCollisions();

        //explosions ==========================================================
        updateAndRenderExplosions(deltaTime);

        // HUD rendering
        updateAndRenderHUD();

        batch.end();
    }

    private void updateAndRenderHUD() {
        // top row
        font.draw(batch, "Score", HUDLeftX, HUDRow1Y, HUDSectionWidth, Align.left, false);
        font.draw(batch, "Shield", HUDCentreX, HUDRow1Y, HUDSectionWidth, Align.center, false);
        font.draw(batch, "Lives", HUDRightX, HUDRow1Y, HUDSectionWidth, Align.right, false);

        // second row
        font.draw(batch, String.format(Locale.getDefault(), "%06d", score), HUDLeftX, HUDRow2Y, HUDSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.shield), HUDCentreX, HUDRow2Y, HUDSectionWidth, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives), HUDRightX, HUDRow2Y, HUDSectionWidth, Align.right, false);
    }

    private void spawnEnemyShips(float deltaTime) {
        enemySpawnTimer += deltaTime;

        if (enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShips.add(new EnemyShip(ShootEmUp.random.nextFloat() * (WORLD_WIDTH - 10) + 5, WORLD_HEIGHT - 5,
                    10, 10,
                    48, 1,
                    .3f, 5, 50, .8f,
                    enemyShipTextureRegion, enemyShieldTextureRegion, enemyLaserTextureRegion));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }

    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {

        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float) WORLD_HEIGHT / 2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.direction.x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.direction.y * enemyShip.movementSpeed * deltaTime;

        if (xMove > 0)
            xMove = Math.min(xMove, rightLimit);
        else
            xMove = Math.max(xMove, leftLimit);

        if (yMove > 0)
            yMove = Math.min(yMove, upLimit);
        else
            yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }


    private void detectInput(float deltaTime) {

        //keyboard
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = (float) WORLD_HEIGHT / 2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

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
        if (Gdx.input.isTouched()) {
            // get screen position
            float xTouchPixels = Gdx.input.getX();
            float yTouchPixels = Gdx.input.getY();

            //convert to world position
            Vector2 touchPoint = new Vector2(xTouchPixels, yTouchPixels);
            touchPoint = viewport.unproject(touchPoint);

            //calculate x + y differences
            Vector2 playerShipCentre = new Vector2(playerShip.boundingBox.x + playerShip.boundingBox.width / 2,
                    playerShip.boundingBox.y + playerShip.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > TOUCH_MOVEMENT_THRESHOLD) {
                float xTouchDifference = touchPoint.x - playerShipCentre.x;
                float yTouchDifference = touchPoint.y - playerShipCentre.y;

                // scale to max speed of ship
                float xMove = xTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDifference / touchDistance * playerShip.movementSpeed * deltaTime;

                if (xMove > 0)
                    xMove = Math.min(xMove, rightLimit);
                else
                    xMove = Math.max(xMove, leftLimit);

                if (yMove > 0)
                    yMove = Math.min(yMove, upLimit);
                else
                    yMove = Math.max(yMove, downLimit);

                playerShip.translate(xMove, yMove);
            }
        }

    }

    private void detectCollisions() {
        //for each player laser check if it intersects enemy ship
        ListIterator<Laser> lasersIterator = playerLasers.listIterator();
        while (lasersIterator.hasNext()) {
            Laser laser = lasersIterator.next();

            ListIterator<EnemyShip> enemyShipsIterator = enemyShips.listIterator();
            while (enemyShipsIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipsIterator.next();

                if (enemyShip.intersects(laser.boundingBox)) {
                    if (enemyShip.hitAndCheckDestroy(laser)) {
                        enemyShipsIterator.remove();
                        explosions.add(new Explosion(explosionTexture, new Rectangle(enemyShip.boundingBox), 0.7f));
                        score += 100;
                    }

                    lasersIterator.remove();
                    break;
                }
            }
        }

        //for each enemy laser check if it intersects player ship
        lasersIterator = enemyLasers.listIterator();
        while (lasersIterator.hasNext()) {
            Laser laser = lasersIterator.next();
            if (playerShip.intersects(laser.boundingBox)) {

                if (playerShip.hitAndCheckDestroy(laser)) {
                    explosions.add(new Explosion(explosionTexture, new Rectangle(playerShip.boundingBox), 1.6f));

                    playerShip.shield = 2;
                    playerShip.lives--; // TODO: GAME OVER IF ZERO
                }

                lasersIterator.remove();
            }
        }
    }

    private void updateAndRenderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionIterator = explosions.listIterator();
        while (explosionIterator.hasNext()) {
            Explosion explosion = explosionIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionIterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }

    private void renderLasers(float deltaTime) {
        // create new lasers ==================================================
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            for (Laser laser : lasers) {
                playerLasers.add(laser);
            }
        }


        ListIterator<EnemyShip> enemyShipsIterator = enemyShips.listIterator();
        while (enemyShipsIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipsIterator.next();
            if (enemyShip.canFireLaser())
                enemyLasers.addAll(Arrays.asList(enemyShip.fireLasers()));
        }

        // remove old lasers ==================================================
        ListIterator<Laser> iterator = playerLasers.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT)
                iterator.remove();
        }
        iterator = enemyLasers.listIterator();
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
