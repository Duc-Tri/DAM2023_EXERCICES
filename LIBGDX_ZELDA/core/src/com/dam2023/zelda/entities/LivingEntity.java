package com.dam2023.zelda.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.tiles.Tile;

/**
 * Une entité vivante pouvant se déplacer, prendre des dégats et en infliger
 */
public abstract class LivingEntity extends Entity
{
    // Vie totale
    protected final float maxLife;

    // Dégats infligés
    protected final float damage;

    // Vitesse de déplacement
    protected final float moveSpeed;

    // Ensemble des frames de textures possible
    public TextureRegion[][] textureFrames;

    // Animations de déplacement
    public Animation animMoveTop;
    public Animation animMoveBottom;
    public Animation animMoveLeft;
    public Animation animMoveRight;

    // Animation de déplacement (avec dégats subis)
    public Animation animMoveTopDamaged;
    public Animation animMoveBottomDamaged;
    public Animation animMoveLeftDamaged;
    public Animation animMoveRightDamaged;

    // Animation de mort
    protected Animation animDeadRotate;

    public LivingEntity(String textureName, String name, int id, int maxLife, float damage, float moveSpeed)
    {
        super(textureName, name, id);
        this.maxLife = maxLife;
        this.damage = damage;
        this.moveSpeed = moveSpeed;
    }

    public float getMoveSpeed()
    {
        return moveSpeed;
    }

    public float getDamage()
    {
        return damage;
    }

    public Rectangle getDamageBounds(float posX, float posY)
    {
        return new Rectangle(posX * Tile.TILE_SIZE + 2, posY * Tile.TILE_SIZE + 1, 13, 14);
    }

    public float getMaxLife()
    {
        return maxLife;
    }
}
