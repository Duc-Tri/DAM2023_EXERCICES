package com.dam2023.zelda.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.entities.instances.InstanceEntity;
import com.dam2023.zelda.map.Chunk;
import com.dam2023.zelda.map.Map;
import com.dam2023.zelda.tiles.Tile;
import com.dam2023.zelda.tiles.Tiles;
import com.dam2023.zelda.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Aurelien on 14/01/2016.
 */
public abstract class Structure
{
    // Le chemin qui mène aux tiles dans les assets
    protected static final String PATH = "sprites/structures/";

    private static final List replaceableTiles = Arrays.asList(Tiles.herbe, Tiles.fleurs, Tiles.hautes_herbes);

    public final int id;
    public final String name;
    public final Texture texture;

    protected ArrayList<Rectangle> collisionRectangles;

    public Structure(String name, int id)
    {
        this.id = id;
        this.name = name;
        this.texture = new Texture(Gdx.files.internal(PATH + this.name + ".png"));
    }

    public ArrayList<Rectangle> getCollisionRectangles(float x, float y)
    {
        collisionRectangles = new ArrayList<>();
        collisionRectangles.add(new Rectangle(x, y, texture.getWidth(), texture.getHeight()));
        return collisionRectangles;
    }

    public void place(Map map, int chunkX, int chunkY, int x, int y)
    {
        Chunk chunk = map.chunks.get(chunkX).get(chunkY);
        chunk.structures.add(newInstance(x * Tile.TILE_SIZE + chunkX * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE, y * Tile.TILE_SIZE + chunkY * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE));
    }

    public void placeRandom(int chunkX, int chunkY)
    {
        boolean placed = false;
        int x = 0;
        int y = 0;
        int iterations = 0;
        while (!placed && iterations < 5)
        {
            placed = true;
            x = World.random.nextInt(Chunk.CHUNK_TILE_SIZE);
            y = World.random.nextInt(Chunk.CHUNK_TILE_SIZE);
            ArrayList<Rectangle> rectangles = getCollisionRectangles(x * Tile.TILE_SIZE + chunkX * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE, y * Tile.TILE_SIZE + chunkY * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE);
            for (Rectangle rectangle : rectangles)
            {
                // Collisions avec les autres structures
                for (int i = chunkX - 1; i <= chunkX + 1 && placed; i++)
                {
                    for (int j = chunkY - 1; j <= chunkY + 1 && placed; j++)
                    {
                        try
                        {
                            Chunk chunk = World.getCurrentMap().chunks.get(i).get(j);
                            for (InstanceStructure structure : chunk.structures)
                            {
                                for (Rectangle collision : structure.collisions)
                                {
                                    if (Intersector.overlaps(rectangle, collision))
                                    {
                                        placed = false;
                                    }
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            // Le chunk n'existe pas, pas grave
                        }
                    }
                }
                // Collisions avec les entités
                for (InstanceEntity instanceCollision : World.getCurrentMap().entities)
                {
                    if (Intersector.overlaps(rectangle, instanceCollision.getCollisionBounds()))
                    {
                        placed = false;
                    }
                }
                // Collision avec le héros
                if (Intersector.overlaps(rectangle, World.getHero().getCollisionBounds()))
                {
                    placed = false;
                }
            }
            iterations++;
        }
        if (iterations != 5)
        {
            World.getCurrentMap().chunks.get(chunkX).get(chunkY).structures.add(newInstance(x * Tile.TILE_SIZE + chunkX * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE, y * Tile.TILE_SIZE + chunkY * Chunk.CHUNK_TILE_SIZE * Tile.TILE_SIZE));
        }
    }

    public InstanceStructure newInstance(float x, float y)
    {
        return new InstanceStructure(this, x, y);
    }
}
