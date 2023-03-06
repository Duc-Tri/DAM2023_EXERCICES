package com.libgdx.pathfinder;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import java.util.ArrayList;
import java.util.List;

public class MyTiledMap {
    private AStarTiledMap myAStarTiledMap;

    // MAP QUI CONTIENT TOUS LES BLOCS DU LABYRINTHE
    //-------------------------------------------------------------------------

    /*

    - on nous donnes les blocs (par exemple 3*3 blocs de 5*5 tuiles)
    - on crée un tableau de la taille des blocs (nb.blocs * nb.tuiles, en longueur et largeur donc ici : 15*15)
    - on copie les tuiles de chaque bloc dans ce tableau (de tous les layers)
    - on fait fonctionner le RENDERING et le PATHFINDING sur ce tableau (labyrinthe final)

     */

    TiledMap myMap;
    MapLayers myLayersContainer;
    int nbBlocWidth, nbBlocHeight;
    private int myMapWidth;
    private int myMapHeight;

    // TEMPORAIRE
    private Vector2int startPoint, endPoint;
    public TiledMapTileLayer myLabyrinthLayer; // public for the moment ...

    public List<Vector2int> solution; // public for the moment ...

    public MyTiledMap(List<String> blocFileNames, int nBlocWidth, int nBlocHeight) {

        if (blocFileNames.size() != nBlocHeight * nBlocWidth) {
            System.err.println("LE NOMBRE DE BLOCS NE CORRESPOND PAS AUX DIMENSIONS FINALES DU LABYRINTHE !!! " +
                    blocFileNames.size() + " != " + (nBlocHeight * nBlocWidth));
            return;
        }

        nbBlocHeight = nBlocHeight;
        nbBlocWidth = nBlocWidth;

        // 1] CRETION DE LISTE DES MAPS AVEC LES BLOCS =======================================
        //Création d'une liste pour stocker les bloc de type TiledMap
        List<TiledMap> blocMapList = new ArrayList<>();

        //Je parcours la liste de string reçu pour remplir ma liste de TiledMap
        for (String blocActuel : blocFileNames) {
            //J'ajoute dans ma liste
            blocMapList.add(new TmxMapLoader().load(blocActuel));
        }

        // 2] LECTURE DES DONNÉES DU PREMIER BLOC =============================================
        /*
        CONTRAINTES :
        ■ Les paramètres de tilesets et les dimensions du PREMIER bloc sont les mêmes
        pour les autres blocs (ils sont tous uniformes).
        ■ On a UN SEUL tileset (jeu de tuiles) dans les blocs
        ■ On a deux (et seulement deux) layers par bloc, et donc dans le labyrinthe final
        ■ ON S'APPUIE SUR TOUT CES CONTRAINTES POUR CONSTRUIRE NOTRE LABYRINTHE.
        */

        // 3] CONSTRUCTION DE LA NOUVELLE TILEDMAP =================================================
        MapProperties map0Properties = blocMapList.get(0).getProperties();
        TiledMap bloc0 = blocMapList.get(0);
        MapLayer layerBloc0 = bloc0.getLayers().get(0);
        TiledMapTileSets containerTilesetsBloc0 = bloc0.getTileSets();

        // Construit le TMX avec un layer, de
        myMap = new TiledMap();
        myLayersContainer = myMap.getLayers();
        int nbTilesX = (int) (map0Properties.get("width"));
        int nbTilesY = (int) (map0Properties.get("height"));
        myMapWidth = nbTilesX * nbBlocWidth;
        myMapHeight = nbTilesY * nbBlocHeight;
        myLabyrinthLayer = new TiledMapTileLayer(myMapWidth, myMapHeight,
                (int) map0Properties.get("tileheight"),
                (int) map0Properties.get("tilewidth"));

        myLayersContainer.add(myLabyrinthLayer);

        // Récupère le conteneur des tilesets pour l'initialiser
        TiledMapTileSets tilesetsContainer = myMap.getTileSets();
        tilesetsContainer.addTileSet(bloc0.getTileSets().getTileSet(0)); // SEULEMENT UN TILESET !!!

        System.out.println("LABY ***** DIM:  " + myLabyrinthLayer.getWidth() + " / " + myLabyrinthLayer.getHeight() +
                " ***** TILE:  " + myLabyrinthLayer.getTileHeight() + " / " + myLabyrinthLayer.getTileWidth());

        int labyCellY = 0; // ordonnée du Cell dans la labyrinthe
        int labyCellX = 0; // abscisse du Cell dans la labyrinthe

        int nBlocX = 0; // de 0 a nbrBlocWidth
        int nBlocY = 0; // de 0 a nbrBlocheight

        TiledMapTileLayer.Cell blocCell = null;

        for (TiledMap currentBloc : blocMapList) {

            TiledMapTileLayer blocLayer2 = (TiledMapTileLayer) currentBloc.getLayers().get(1); // LE 2E LAYER, LES OBSTACLES

            int layerHeight = blocLayer2.getHeight(); // NORMALEMENT TOUJOURS LE MEME POUR TOUS LES MAPS

            int layerWidth = blocLayer2.getWidth(); // NORMALEMENT TOUJOURS LE MEME POUR TOUS LES MAPS

            for (int cellBlocX = 0; cellBlocX < layerWidth; cellBlocX++) {
                for (int cellBlocY = 0; cellBlocY < layerHeight; cellBlocY++) {

                    blocCell = blocLayer2.getCell(cellBlocX, cellBlocY);

                    labyCellX = nBlocX * nbTilesX + cellBlocX;
                    labyCellY = nBlocY * nbTilesY + cellBlocY;

//                            ((nbBlocHeight - nBlocY) * nbTilesY) -                            (nBlocY * nbTilesY + cellBlocY);
                    myLabyrinthLayer.setCell(labyCellX, labyCellY, blocCell);

                }
            }

            // passage au bloc suivant, on recalcule la position du bloc dans le laby
            if (++nBlocX == nbBlocWidth) {
                nBlocX = 0; // on peut utiliser MODULO
                nBlocY++;
            }

        }

        myAStarTiledMap = new AStarTiledMap(this);

    }

    // Fonction temporaire très moche !
    public void mouseClicked(int screenX, int screenY, int button) {

        int cellX = screenX / 16; // TEMP
        int cellY = screenY / 16; // screenX, screenY

        if (cellX >= myLabyrinthLayer.getWidth() || cellY >= myLabyrinthLayer.getHeight() ||
                myLabyrinthLayer.getCell(cellX, cellY) != null)
            return;

        System.out.println("mouseClicked === " + cellX + "/" + cellY + " ### " + screenX + "/" + screenY);

        switch (button) {
            case 0: // bouton de gauche
                setStartPoint(cellX, cellY);
                break;

            case 1: // bouton de droite
                setFinishPoint(cellX, cellY);
                break;

            case 2: // bouton du milieu
                setWallOrEmpty(cellX, cellY);
                break;
        }
    }

    private void setStartPoint(int cellX, int cellY) {
        startPoint = new Vector2int(cellX, cellY);
        if (endPoint != null)
            TestPathfinding();
    }

    private void setFinishPoint(int cellX, int cellY) {
        endPoint = new Vector2int(cellX, cellY);
        if (startPoint != null)
            TestPathfinding();
    }

    private void TestPathfinding() {

//        myLabyrinthLayer

        solution = myAStarTiledMap.FindPath(new Node(startPoint, null), new Node(endPoint, null));

        /*
        String s = ""; // temp str
        for (Vector2int v : solution) {
            s += v.myX + "/" + v.myY + " ";
        }
        System.out.println("SOLUTION ### " + s);
         */
    }

    private static void setWallOrEmpty(int cellX, int cellY) {

    }

    public TiledMap getTiledMap() {
        return myMap;
    }
}
