package com.libgdx.roguelike;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;


public class libGDXRoguelike extends ApplicationAdapter implements InputProcessor {
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    Viewport viewport;
    int SCREEN_WIDTH = 0;
    int SCREEN_HEIGHT = 0;
    SpriteBatch batch;
    TextureAtlas textureAtlas;
    Sprite playerSprite;
    Player player;
    TextureRegion textureRegion;
    int speed = 80;
    int calculatedWidth =0;
    int calculatedHeight =0;
    @Override
    public void resize(int width, int height) {
       // viewport.update(width, height);
    }
    @Override
    public void create() {
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        tiledMap = new TmxMapLoader().load("sampleMap.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tiledMapRenderer.render();
        initializeCharacter();
        Iterator<String> it = tiledMap.getProperties().getKeys();
        while(it.hasNext()){
            System.out.println(it.next());
        }
        int widthMap = Integer.parseInt(tiledMap.getProperties().get("width")+"");
        int heightMap = Integer.parseInt(tiledMap.getProperties().get("height")+"");
        int tilewidth = Integer.parseInt(tiledMap.getProperties().get("tilewidth")+"");
        int tileheight = Integer.parseInt(tiledMap.getProperties().get("tileheight")+"");

        calculatedWidth = widthMap*tilewidth;
        calculatedHeight = heightMap*tileheight;

        Gdx.input.setInputProcessor(this);
    }
    private void initializeCharacter() {

        player =  new Player();
        player.initializeSprite();
        batch = player.getBatch();
        playerSprite = player.getSprite();
        textureAtlas = player.getTextureAtlas();
        textureRegion = player.getTextureRegion();
        playerSprite.setPosition(50, 50);

    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0f, 0f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.begin();
        playerSprite.draw(batch);
        batch.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT){
            player.checkSprite("LEFT");
            System.out.println("LEFT");
            player.setX(player.getX() - speed);
            if(player.getX()<SCREEN_WIDTH*1/4) {
                if(camera.position.x<SCREEN_WIDTH*1/4){
                    if(player.getX()>0) {
                        player.setX(player.getX() + speed);
                    }
                }else{
                    player.setX(player.getX() + speed);
                    camera.position.x -= speed;
                }
            }
        }
        if (keycode == Input.Keys.RIGHT){
            player.checkSprite("RIGHT");
            System.out.println("RIGHT");
            player.setX(player.getX() + speed);
            if(player.getX()>SCREEN_WIDTH*3/4) {
                if(camera.position.x>calculatedWidth-SCREEN_WIDTH*1/4){
                    if(player.getX()<SCREEN_WIDTH) {
                        player.setX(player.getX() - speed);
                    }
                }else{
                    player.setX(player.getX() - speed);
                    camera.position.x += speed;
                }
            }
        }
        if (keycode == Input.Keys.UP){
            player.checkSprite("UP");
            System.out.println("UP");
            player.setY(player.getY() + speed);
            if(player.getY()>SCREEN_HEIGHT*3/4) {
                if(camera.position.y>calculatedHeight-SCREEN_HEIGHT*1/4){
                    if(player.getY()<SCREEN_HEIGHT) {
                        player.setY(player.getY() - speed);
                    }
                }else{
                    player.setY(player.getY() - speed);
                    camera.position.y += speed;
                }
            }
        }
        if (keycode == Input.Keys.DOWN){
            player.checkSprite("DOWN");
            System.out.println("DOWN");
            player.setY(player.getY() - speed);
            if(player.getY()<SCREEN_HEIGHT*1/4) {
                if(camera.position.y<SCREEN_HEIGHT*1/4){
                    if(player.getY()>0) {
                        player.setY(player.getY() + speed);
                    }
                }else{
                    player.setY(player.getY() + speed);
                    camera.position.y -= speed;
                }
            }

        }
        System.out.println();
        batch.begin();
        playerSprite = player.getSprite();
        playerSprite.draw(batch);
        batch.end();
        camera.update();
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
        System.out.println("mouseMoved ");
        return false;
    }
    @Override
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("scrolled ");
        return false;
    }
}