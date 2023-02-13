package com.libgdx.roguelike;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


public class Player  {
    int compteurUp = 0;
    int compteurDown = 0;
    int compteurLeft = 0;
    int compteurRight = 0;
    private Rectangle box;
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TextureRegion textureRegion;
    private Sprite sprite;
    private float x = 0;
    private float y = 0;
    public void initializeSprite() {
        box = new Rectangle(0,0,0,0);
        batch = new SpriteBatch();
        textureAtlas = new TextureAtlas(Gdx.files.internal("tiny_16x16.atlas"));
        textureRegion = textureAtlas.findRegion("UP_1");
        sprite = new Sprite(textureRegion);
        sprite.scale(2.0f);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public SpriteBatch getBatch() {
        return batch;
    }
    public TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }
    public void setX(float x){
        this.x = x;
        box.setX(x);
        sprite.setX(x);
    }

    public void setY(float y){
        this.y = y;
        box.setY(y);
        sprite.setY(y);
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void checkSprite(String string) {
        float tempSpriteX = sprite.getX();
        float tempSpriteY = sprite.getY();


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
        tempSprite.scale(2.0f);
        this.setSprite( tempSprite);
//        playerSprite = player.getSprite();
    }

}
