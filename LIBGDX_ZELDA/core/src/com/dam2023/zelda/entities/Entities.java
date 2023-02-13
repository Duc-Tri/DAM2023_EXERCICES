package com.dam2023.zelda.entities;

import java.util.HashMap;

/**
 * On stocke ici toutes les entitées enregistrés dans le jeu
 */
public class Entities
{
    private static int currentId = 0;

    public static EntityHero hero;
    public static EntityRock rock;
    public static EntityHostileMonster blueMoblin;
    public static EntityHostileMonster redMoblin;

    private static HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();

    public static void registerEntities()
    {
        hero = new EntityHero("link", "Héros", currentId++, 10, 1, 4f);
        rock = new EntityRock(currentId++);
        blueMoblin = new EntityMoblin("blue_moblin", "Moblin Bleu", currentId++, 10, 0.5f, 2f, EntityMoblin.MoblinType.BLUE);
        redMoblin = new EntityMoblin("red_moblin", "Moblin Rouge", currentId++, 10, 0.5f, 2f, EntityMoblin.MoblinType.RED);

        entities.put(hero.id, hero);
        entities.put(rock.id, rock);
        entities.put(blueMoblin.id, blueMoblin);
        entities.put(redMoblin.id, redMoblin);
    }

    public static Entity getEntity(Integer id)
    {
        return entities.get(id);
    }
}
