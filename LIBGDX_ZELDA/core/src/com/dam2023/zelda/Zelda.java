package com.dam2023.zelda;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.dam2023.zelda.animations.Animations;
import com.dam2023.zelda.entities.Entities;
import com.dam2023.zelda.items.Items;
import com.dam2023.zelda.save.Save;
import com.dam2023.zelda.screens.GameScreen;
import com.dam2023.zelda.sound.Musics;
import com.dam2023.zelda.sound.Sounds;
import com.dam2023.zelda.structures.Structures;
import com.dam2023.zelda.tiles.Tiles;
import com.dam2023.zelda.world.World;

/**
 * La classe principale du jeu
 */
public class Zelda extends Game
{
    @Override
    public void create()
    {
        // Phase d'initialisation des assets
        Animations.registerAnimations();
        Entities.registerEntities();
        Tiles.registerTiles();
        Musics.registerMusics();
        Sounds.registerSounds();
        Items.registerItems();
        Structures.registerStructures();

        // Charger la sauvegarde si elle existe
        Save.loadOrCreateSave();

        // Initialisation du jeu
        GameScreen gameScreen = new GameScreen(this);

        // Set first screen
        this.setScreen(gameScreen);

        // Faire du héros (instance) la classe qui gère les inputs
        Gdx.input.setInputProcessor(World.getHero());
    }

    @Override
    public void render()
    {
        super.render();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void dispose()
    {

    }


}
