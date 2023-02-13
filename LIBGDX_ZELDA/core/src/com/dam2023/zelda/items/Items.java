package com.dam2023.zelda.items;

import java.util.HashMap;

/**
 * Created by Aurelien on 12/01/2016.
 */
public class Items
{
    private static int currentId = 0;

    public static ItemSword sword;

    private static HashMap<Integer, Item> items = new HashMap<Integer, Item>();

    public static void registerItems()
    {
        sword = new ItemSword("sword", "Epée", currentId++, 0.15f, 22f);

        items.put(sword.id, sword);
    }
}
