package com.dam2023.zelda.world;

import com.dam2023.zelda.entities.instances.InstanceEntityHero;
import com.dam2023.zelda.map.Map;

import java.util.Random;

/**
 * Le monde, contenant des champs statiques map et héros
 */
public class World
{
    // La map à laquelle se trouve le héros
    private static Map currentMap;
    // Le Heros à afficher
    private static InstanceEntityHero hero;
    // Le variable servant à la génération d'évenements Randoms
    public static Random random = new Random();

    public static Map getCurrentMap()
    {
        return currentMap;
    }

    public static void setCurrentMap(Map currentMap)
    {
        World.currentMap = currentMap;
    }

    public static void initHero()
    {
        World.hero = new InstanceEntityHero();
    }

    public static InstanceEntityHero getHero()
    {
        return hero;
    }
}
