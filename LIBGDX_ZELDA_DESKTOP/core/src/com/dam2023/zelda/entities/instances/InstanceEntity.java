package com.dam2023.zelda.entities.instances;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.entities.Entity;
import com.dam2023.zelda.map.Chunk;
import com.dam2023.zelda.tiles.Tile;
import com.dam2023.zelda.util.Math;

/**
 * Permet de créer une entité unique, ajoute les champs qui seront dépendant à chaque instance d'une entité donnée
 */
public class InstanceEntity
{
    protected Entity entity;

    // Coordonnée x en Tiles
    public float x;
    // Coordonnée y en Tiles
    public float y;

    public InstanceEntity(Entity entity)
    {
        this.entity = entity;
        this.x = 0;
        this.y = 0;
    }

    public InstanceEntity(Entity entity, float x, float y)
    {
        this.entity = entity;
        this.x = x;
        this.y = y;
    }

    public void draw(SpriteBatch batch, float deltaTime)
    {
        entity.draw(batch, this.x * Tile.TILE_SIZE, this.y * Tile.TILE_SIZE);
    }

    /**
     * Retourne la position X de l'entité courante par rapport à un autre entité servant de référentiel
     */
    public float getXRelativeTo(InstanceEntity entity)
    {
        return entity.x - this.x;
    }

    /**
     * Retourne la position Y de l'entité courante par rapport à un autre entité servant de référentiel
     */
    public float getYRelativeTo(InstanceEntity entity)
    {
        return entity.y - this.y;
    }

    /**
     * Retourne la position X en Chunks
     */
    public int getXChunk()
    {
        return Math.floorDiv((int)x, Chunk.CHUNK_TILE_SIZE);
    }

    /**
     * Retourne la position Y en Chunks
     */
    public int getYChunk()
    {
        return Math.floorDiv((int)y, Chunk.CHUNK_TILE_SIZE);
    }

    public Rectangle getCollisionBounds()
    {
        return entity.getCollisionBounds(x, y);
    }

    public void update()
    {

    }

    public int getEntityId()
    {
        return entity.getId();
    }
}
