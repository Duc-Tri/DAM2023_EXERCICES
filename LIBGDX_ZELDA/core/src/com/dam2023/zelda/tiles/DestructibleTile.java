package com.dam2023.zelda.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Un Tile qui pourra être détruit - Classe à peine commencée
 */
public class DestructibleTile extends Tile
{
    protected float destroyTime;
    protected Animation destroyAnimation;

    public DestructibleTile(String name, int id, float destroyTime, Animation destroyAnimation)
    {
        super(name, id);
        this.destroyTime = destroyTime;
        this.destroyAnimation = destroyAnimation;
    }

    @Override
    public void draw(SpriteBatch batch, float deltaTime, int xPos, int yPos)
    {
        super.draw(batch, deltaTime, xPos, yPos);
    }
}
