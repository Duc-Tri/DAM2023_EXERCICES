package com.dam2023.zelda.structures;

import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.tiles.Tile;
import com.dam2023.zelda.tiles.Tiles;

import java.util.ArrayList;

/**
 * Created by Aurelien on 14/01/2016.
 */
public class StructureHouse extends Structure
{

    public StructureHouse(String name, int id)
    {
        super(name, id);
    }

    @Override
    public ArrayList<Rectangle> getCollisionRectangles(float x, float y)
    {
        collisionRectangles = new ArrayList<Rectangle>();
        collisionRectangles.add(new Rectangle(x,y,16,16));
        collisionRectangles.add(new Rectangle(x+16,y+8,16,8));
        collisionRectangles.add(new Rectangle(x+32,y,16,15));
        collisionRectangles.add(new Rectangle(x,y+16,48,32));
        return collisionRectangles;
    }
}
