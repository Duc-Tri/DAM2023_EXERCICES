package com.dam2023.zelda.tiles;

import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.HashMap;

/**
 * La liste des tiles enregistrés et initialisés au lancement du jeu
 */
public class Tiles {
    private static int currentId = 0;

    public static Tile herbe;
    public static Tile hautes_herbes;
    public static Tile terre;
    public static Tile buisson;
    public static Tile arbrisseau;
    public static Tile cailloux;
    public static Tile graviers;
    public static Tile fleurs;

    private static final HashMap<Integer, Tile> tiles = new HashMap<>();

    public static void registerTiles() {
        herbe = new Tile("herbe", currentId++);
        hautes_herbes = new Tile("hautes_herbes", currentId++);
        terre = new Tile("terre", currentId++);
        buisson = new Tile("buisson", currentId++);
        arbrisseau = new Tile("arbrisseau", currentId++);
        cailloux = new Tile("cailloux", currentId++);
        graviers = new Tile("graviers", currentId++);
        fleurs = new AnimatedTile("fleurs", currentId++, 0.25f, Animation.PlayMode.LOOP);

        tiles.put(herbe.getId(), herbe);
        tiles.put(hautes_herbes.getId(), hautes_herbes);
        tiles.put(terre.getId(), terre);
        tiles.put(buisson.getId(), buisson);
        tiles.put(arbrisseau.getId(), arbrisseau);
        tiles.put(cailloux.getId(), cailloux);
        tiles.put(graviers.getId(), graviers);
        tiles.put(fleurs.getId(), fleurs);
    }

    public static Tile getTile(int id) {
        return tiles.get(id);
    }
}
