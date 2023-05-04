package com.dam2023.zelda.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.entities.Entities;
import com.dam2023.zelda.entities.Entity;
import com.dam2023.zelda.entities.instances.InstanceEntity;
import com.dam2023.zelda.structures.InstanceStructure;
import com.dam2023.zelda.structures.Structures;
import com.dam2023.zelda.tiles.Tile;
import com.dam2023.zelda.tiles.Tiles;
import com.dam2023.zelda.world.World;

import java.util.ArrayList;

/**
 * Stocke un morceau de monde
 */
public class Chunk {
    // La taille d'un côté du chunk en tiles
    public static final int CHUNK_TILE_SIZE = 10;

    // Tiles formants le chunk
    protected final Tile[][] tiles;

    // Positions
    public final int x;
    public final int y;

    // Structures
    public final ArrayList<InstanceStructure> structures = new ArrayList<>();

    public Chunk(int x, int y) {
        this.x = x;
        this.y = y;

        tiles = new Tile[CHUNK_TILE_SIZE][CHUNK_TILE_SIZE];

        for (int i = 0; i < CHUNK_TILE_SIZE; i++) {
            for (int j = 0; j < CHUNK_TILE_SIZE; j++) {
                double rand = Math.random() * 100;
                if (rand < 10) {
                    tiles[i][j] = Tiles.fleurs;
                } else {
                    tiles[i][j] = Tiles.herbe;
                }
            }
        }
    }

    public void initRandomStructures() {
        if (Math.random() < 0.05) {
            Structures.red_house.placeRandom(x, y);
        }
        int nbArbres = World.random.nextInt(4) + 4;
        for (int i = 0; i < nbArbres; i++) {
            Structures.tree.placeRandom(x, y);
        }
    }

    public void drawTiles(SpriteBatch batch, float deltaTime, int xChunk, int yChunk) {
        int xMargin = xChunk * Tile.TILE_SIZE * CHUNK_TILE_SIZE;
        int yMargin = yChunk * Tile.TILE_SIZE * CHUNK_TILE_SIZE;

        for (int i = 0; i < CHUNK_TILE_SIZE; i++) {
            for (int j = 0; j < CHUNK_TILE_SIZE; j++) {
                int xPos = xMargin + i * Tile.TILE_SIZE;
                int yPos = yMargin + j * Tile.TILE_SIZE;

                tiles[i][j].draw(batch, deltaTime, xPos, yPos);
            }
        }
    }

    public void drawStructures(SpriteBatch batch) {
        for (InstanceStructure structure : structures) {
            structure.draw(batch);
        }
    }

    public void placeMonstersRandom() {
        if (Math.random() < 0.5) {
            placeEntityRandom(Entities.blueMoblin);
        }
        if (Math.random() < 0.5) {
            placeEntityRandom(Entities.redMoblin);
        }
    }

    public void placeEntityRandom(Entity entity) {
        boolean placed = false;
        int xTile;
        int yTile;
        int iterations = 0;
        InstanceEntity instance = null;
        while (!placed && iterations < 5) {
            placed = true;
            xTile = World.random.nextInt(Chunk.CHUNK_TILE_SIZE) + x * Chunk.CHUNK_TILE_SIZE;
            yTile = World.random.nextInt(Chunk.CHUNK_TILE_SIZE) + y * Chunk.CHUNK_TILE_SIZE;
            instance = entity.newInstance(xTile, yTile);
            Rectangle rectangle = instance.getCollisionBounds();
            // Collision avec les structures
            for (int i = x - 1; i <= x + 1 && placed; i++) {
                for (int j = y - 1; j <= y + 1 && placed; j++) {
                    try {
                        Chunk chunk = World.getCurrentMap().chunks.get(i).get(j);
                        for (InstanceStructure structure : chunk.structures) {
                            for (Rectangle collision : structure.collisions) {
                                if (Intersector.overlaps(rectangle, collision)) {
                                    placed = false;
                                }
                            }
                        }

                    } catch (Exception ex) {
                        // Le chunk n'existe pas, pas grave
                    }
                }
            }
            // Collisions avec les autres entités
            for (InstanceEntity instanceCollision : World.getCurrentMap().entities) {
                if (Intersector.overlaps(rectangle, instanceCollision.getCollisionBounds())) {
                    placed = false;
                }
            }
            // Collision avec le héros
            if (Intersector.overlaps(rectangle, World.getHero().getCollisionBounds())) {
                placed = false;
            }

            iterations++;
        }
        if (iterations != 5) {
            World.getCurrentMap().entities.add(instance);
        }
    }

    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }

    public void setTile(int x, int y, Tile tile) {
        tiles[x][y] = tile;
    }
}
