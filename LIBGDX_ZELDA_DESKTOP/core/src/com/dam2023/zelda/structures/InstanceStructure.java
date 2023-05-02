package com.dam2023.zelda.structures;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by Aurelien on 17/01/2016.
 */
public class InstanceStructure
{
    public final Structure structure;
    public final float x;
    public final float y;

    public final ArrayList<Rectangle> collisions;

    public InstanceStructure(Structure structure, float x, float y)
    {
        this.structure = structure;
        this.x = x;
        this.y = y;
        this.collisions = structure.getCollisionRectangles(x,y);
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(structure.texture, x, y);
    }

    public int getStructureId()
    {
        return structure.id;
    }
}
