package com.dam2023.zelda.entities;

import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.entities.instances.InstanceEntity;
import com.dam2023.zelda.entities.instances.InstanceEntityRock;
import com.dam2023.zelda.tiles.Tile;

/**
 * Created by Aurelien on 20/12/2015.
 */
public class EntityRock extends Entity {
    public EntityRock(int id) {
        super("rocher", "Rocher", id);
    }

    @Override
    public Rectangle getCollisionBounds(float posX, float posY) {
        return new Rectangle(posX * Tile.TILE_SIZE, posY * Tile.TILE_SIZE, 16, 16);
    }

    @Override
    public InstanceEntity newInstance(float x, float y) {
        return new InstanceEntityRock(x, y);
    }
}
