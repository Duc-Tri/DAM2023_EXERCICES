package com.dam2023.zelda.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Aurelien on 12/01/2016.
 */
public class Item
{
    // Identifiant
    protected final int id;

    // Le chemin qui mène aux tiles dans les assets
    protected static final String PATH = "sprites/items/";

    private final String textureName;
    private final String name;

    // La texture chargée
    protected final Texture texture;

    public Item(String textureName, String name, int id)
    {
        this.id = id;
        this.textureName = textureName;
        this.name = name;
        this.texture = new Texture(Gdx.files.internal(PATH + this.textureName + ".png"));
    }

    public String getTextureName()
    {
        return textureName;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Dessine l'entity à la position donnée en Pixels
     */
    public void draw(SpriteBatch batch, float xPos, float yPos)
    {
        batch.draw(texture, xPos, yPos);
    }
}
