package com.dam2023.zelda.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.dam2023.zelda.entities.instances.InstanceEntity;

/**
 * Created by Aurelien on 17/12/2015.
 */
public class Entity
{
    // Identifiant
    protected int id;

    // Le chemin qui mène aux tiles dans les assets
    protected static final String PATH = "sprites/entities/";

    private String textureName;
    private String name;

    // La texture chargée
    protected Texture texture;

    public Entity(String textureName, String name, int id)
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
    public void draw(SpriteBatch batch, float deltaTime, float xPos, float yPos)
    {
        batch.draw(texture, xPos, yPos);
    }

    public Rectangle getCollisionBounds(float posX, float posY)
    {
        return new Rectangle(0,0,0,0);
    }

    public int getId()
    {
        return id;
    }

    public InstanceEntity newInstance(float x, float y)
    {
        return new InstanceEntity(this, x,y);
    }
}
