package com.dam2023.zelda.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.tiles.Tile;

/**
 * L'entité qui va pouvoir être controllée par le joueur
 */
public class EntityHero extends LivingEntity
{
    private static final int regionHeight = 16;
    private static final int regionWidth = 16;

    // Animation de poussage
    public Animation animPushTop;
    public Animation animPushBottom;
    public Animation animPushLeft;
    public Animation animPushRight;

    // Animation de poussage (avec dégats subis)
    public Animation animPushTopDamaged;
    public Animation animPushBottomDamaged;
    public Animation animPushLeftDamaged;
    public Animation animPushRightDamaged;

    // Animation de frappe à l'épée
    public Animation animSwipeSwordTop;
    public Animation animSwipeSwordBottom;
    public Animation animSwipeSwordLeft;
    public Animation animSwipeSwordRight;

    public EntityHero(String textureName, String name, int id, int maxLife, int damage, float moveSpeed)
    {
        super(textureName, name, id, maxLife, damage, moveSpeed);
        this.textureFrames = TextureRegion.split(texture, regionWidth, regionHeight);

        makeAnimations();
    }

    protected void makeAnimations()
    {
        // MOVE
        this.animMoveBottom = new Animation(0.1f, this.textureFrames[0][0], this.textureFrames[0][1]);
        this.animMoveBottom.setPlayMode(Animation.PlayMode.LOOP);

        this.animMoveTop = new Animation(0.1f, this.textureFrames[1][0], this.textureFrames[1][1]);
        this.animMoveTop.setPlayMode(Animation.PlayMode.LOOP);

        this.animMoveLeft = new Animation(0.1f, this.textureFrames[2][0], this.textureFrames[2][1]);
        this.animMoveLeft.setPlayMode(Animation.PlayMode.LOOP);

        this.animMoveRight = new Animation(0.1f, this.textureFrames[3][0], this.textureFrames[3][1]);
        this.animMoveRight.setPlayMode(Animation.PlayMode.LOOP);

        // MOVE DAMAGED
        this.animMoveBottomDamaged = new Animation(0.1f, this.textureFrames[0][2], this.textureFrames[0][3]);
        this.animMoveBottomDamaged.setPlayMode(Animation.PlayMode.LOOP);

        this.animMoveTopDamaged = new Animation(0.1f, this.textureFrames[1][2], this.textureFrames[1][3]);
        this.animMoveTopDamaged.setPlayMode(Animation.PlayMode.LOOP);

        this.animMoveLeftDamaged = new Animation(0.1f, this.textureFrames[2][2], this.textureFrames[2][3]);
        this.animMoveLeftDamaged.setPlayMode(Animation.PlayMode.LOOP);

        this.animMoveRightDamaged = new Animation(0.1f, this.textureFrames[3][2], this.textureFrames[3][3]);
        this.animMoveRightDamaged.setPlayMode(Animation.PlayMode.LOOP);

        // PUSH
        this.animPushBottom = new Animation(0.1f, this.textureFrames[4][0], this.textureFrames[4][1]);
        this.animPushBottom.setPlayMode(Animation.PlayMode.LOOP);

        this.animPushTop = new Animation(0.1f, this.textureFrames[5][0], this.textureFrames[5][1]);
        this.animPushTop.setPlayMode(Animation.PlayMode.LOOP);

        this.animPushLeft = new Animation(0.1f, this.textureFrames[6][0], this.textureFrames[6][1]);
        this.animPushLeft.setPlayMode(Animation.PlayMode.LOOP);

        this.animPushRight = new Animation(0.1f, this.textureFrames[7][0], this.textureFrames[7][1]);
        this.animPushRight.setPlayMode(Animation.PlayMode.LOOP);

        // PUSH DAMAGED
        this.animPushBottomDamaged = new Animation(0.1f, this.textureFrames[4][2], this.textureFrames[4][3]);
        this.animPushBottomDamaged.setPlayMode(Animation.PlayMode.LOOP);

        this.animPushTopDamaged = new Animation(0.1f, this.textureFrames[5][2], this.textureFrames[5][3]);
        this.animPushTopDamaged.setPlayMode(Animation.PlayMode.LOOP);

        this.animPushLeftDamaged = new Animation(0.1f, this.textureFrames[6][2], this.textureFrames[6][3]);
        this.animPushLeftDamaged.setPlayMode(Animation.PlayMode.LOOP);

        this.animPushRightDamaged = new Animation(0.1f, this.textureFrames[7][2], this.textureFrames[7][3]);
        this.animPushRightDamaged.setPlayMode(Animation.PlayMode.LOOP);

        // SWIPE SWORD
        this.animSwipeSwordBottom = new Animation(0.1f, this.textureFrames[10][0], this.textureFrames[10][1], this.textureFrames[10][1]);
        this.animSwipeSwordBottom.setPlayMode(Animation.PlayMode.LOOP);

        this.animSwipeSwordTop = new Animation(0.1f, this.textureFrames[11][0], this.textureFrames[11][1], this.textureFrames[11][1]);
        this.animSwipeSwordTop.setPlayMode(Animation.PlayMode.LOOP);

        this.animSwipeSwordLeft = new Animation(0.1f, this.textureFrames[12][0], this.textureFrames[12][1], this.textureFrames[12][1]);
        this.animSwipeSwordLeft.setPlayMode(Animation.PlayMode.LOOP);

        this.animSwipeSwordRight = new Animation(0.1f, this.textureFrames[13][0], this.textureFrames[13][1], this.textureFrames[13][1]);
        this.animSwipeSwordRight.setPlayMode(Animation.PlayMode.LOOP);

        // DEAD ANIMATION
        this.animDeadRotate = new Animation(0.15f, this.textureFrames[0][0], this.textureFrames[2][0], this.textureFrames[1][0], this.textureFrames[3][0]);
        this.animDeadRotate.setPlayMode(Animation.PlayMode.LOOP);
    }

    @Override
    public Rectangle getCollisionBounds(float posX, float posY)
    {
        return new Rectangle(posX * Tile.TILE_SIZE + 5, posY * Tile.TILE_SIZE + 1, 7, 9);
    }
}
