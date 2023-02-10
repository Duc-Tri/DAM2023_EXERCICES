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
    Sprite character;
    TextureRegion textureRegion;
    int speed = 80;
    int calculatedWidth =0;
    int calculatedHeight =0;

    int compteurUp = 0;
    int compteurDown = 0;
    int compteurLeft = 0;
    int compteurRight = 0;

    @Override
    public void resize(int width, int height) {
       // viewport.update(width, height);
    }
    @Override
    public void create() {

        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();

        System.out.println("SCREEN_WIDTH  " + SCREEN_WIDTH);
        System.out.println("SCREEN_HEIGHT  " + SCREEN_HEIGHT);
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
       batch = new SpriteBatch();
       textureAtlas = new TextureAtlas(Gdx.files.internal("tiny_16x16.atlas"));
       textureRegion = textureAtlas.findRegion("UP_1");
       character = new Sprite(textureRegion);
       character.setPosition(50, 50);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0f, 0f, 0);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
        batch.begin();
//
        character.draw(batch);
        batch.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {




        if (keycode == Input.Keys.LEFT){
            checkSprite("LEFT");
            System.out.println("LEFT");
            character.setX(character.getX() - speed);
            if(character.getX()<SCREEN_WIDTH*1/4) {
                if(camera.position.x<SCREEN_WIDTH*1/4){
                    if(character.getX()>0) {
                        character.setX(character.getX() + speed);
                    }
                }else{
                    character.setX(character.getX() + speed);
                    camera.position.x -= speed;
                }
            }
        }
        if (keycode == Input.Keys.RIGHT){
            checkSprite("RIGHT");
            System.out.println("RIGHT");
           character.setX(character.getX() + speed);
           if(character.getX()>SCREEN_WIDTH*3/4) {
               if(camera.position.x>calculatedWidth-SCREEN_WIDTH*1/4){
                   if(character.getX()<SCREEN_WIDTH) {
                       character.setX(character.getX() - speed);
                   }
               }else{
                   character.setX(character.getX() - speed);
                   camera.position.x += speed;
               }
           }
        }
        if (keycode == Input.Keys.UP){
            checkSprite("UP");
            System.out.println("UP");
            character.setY(character.getY() + speed);
            if(character.getY()>SCREEN_HEIGHT*3/4) {
                if(camera.position.y>calculatedHeight-SCREEN_HEIGHT*1/4){
                    if(character.getY()<SCREEN_HEIGHT) {
                        character.setY(character.getY() - speed);
                    }
                }else{
                    character.setY(character.getY() - speed);
                    camera.position.y += speed;
                }
            }
        }
        if (keycode == Input.Keys.DOWN){
            checkSprite("DOWN");
            System.out.println("DOWN");
            character.setY(character.getY() - speed);
            if(character.getY()<SCREEN_HEIGHT*1/4) {
                if(camera.position.y<SCREEN_HEIGHT*1/4){
                    if(character.getY()>0) {
                        character.setY(character.getY() + speed);
                    }
                }else{
                    character.setY(character.getY() + speed);
                    camera.position.y -= speed;
                }
            }
        }

        System.out.println();
        System.out.println("\n\n\n\n\n");
        System.out.println("SCREEN_WIDTH  " + SCREEN_WIDTH);
        System.out.println("SCREEN_HEIGHT  " + SCREEN_HEIGHT);



        System.out.println("camera.position.x  "+    camera.position.x+"     camera.position.y    "  + camera.position.y    );
        System.out.println("character.getX() "+    character.getX()+"     character.getY()   "  + character.getY()    );
        System.out.println("camera.viewportWidth   " + camera.viewportWidth);
        System.out.println("camera.viewportHeight   " + camera.viewportHeight);



        System.out.println();
        batch.begin();
        character.draw(batch);
        batch.end();
        camera.update();
        return false;
    }

    private void checkSprite(String string) {
        float tempSpriteX = character.getX();
        float tempSpriteY = character.getY();


       if(string.contentEquals("LEFT")) {
           System.out.println("SWITCH LEFT " +compteurLeft);
           if(compteurLeft==12){
               compteurLeft =0;
           }
               compteurDown=0;
               compteurUp=0;
               compteurRight=0;

           compteurLeft++;
           textureRegion = textureAtlas.findRegion("LEFT_"+compteurLeft);

       }

        if(string.contentEquals("RIGHT")) {
            System.out.println("SWITCH RIGHT " +compteurRight);
            if(compteurRight==12){
                compteurRight =0;
            }
            compteurDown=0;
            compteurUp=0;
            compteurLeft=0;
            compteurRight++;
            textureRegion = textureAtlas.findRegion("RIGHT_"+compteurRight);

        }

        if(string.contentEquals("UP")) {
            System.out.println("SWITCH UP " +compteurUp);
            if(compteurUp==12){
                compteurUp =0;
            }
            compteurDown=0;
            compteurRight=0;
            compteurLeft=0;
            compteurUp++;
            textureRegion = textureAtlas.findRegion("UP_"+compteurUp);

        }
        if(string.contentEquals("DOWN")) {
            System.out.println("SWITCH DOWN " +compteurDown);
            if(compteurDown==12){
                compteurDown =0;
            }
            compteurUp=0;
            compteurRight=0;
            compteurLeft=0;
            compteurDown++;
            textureRegion = textureAtlas.findRegion("DOWN_"+compteurDown);

        }



        Sprite tempSprite = new Sprite(textureRegion);
        tempSprite.setX(tempSpriteX);
        tempSprite.setY(tempSpriteY);

        character = tempSprite;
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
    private void resetTempPosition() {
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