package com.example.dam_stream;

import java.util.ArrayList;

/**
 * Liste de boissons, avec des fonctions de sauvegarde et de restauration
 */
public class BoissonsList {

    public static final String FICHIER_SAUVEGARDE_DEFAUT = "boissons_data.csv";
    public final String name;
    private ArrayList<Boisson> listBoissons; // private mais il faudrait getter, setter etc.

    public BoissonsList(String n) {
        name = n;
        listBoissons = new ArrayList<>();
    }

    /**
     * Pour générer une liste de boissons
     */
    public ArrayList<Boisson> GenerateList() {

        if (listBoissons != null)
            listBoissons.clear();

        listBoissons = new ArrayList<>();

        listBoissons.add(new Boisson("café long", 50));
        listBoissons.add(new Boisson("café expresso", 55));
        listBoissons.add(new Boisson("café capuccino", 60));
        listBoissons.add(new Boisson("chocolat", 100));
        listBoissons.add(new Boisson("coca", 90));
        listBoissons.add(new Boisson("orangina", 99));

        return listBoissons;
    }

    public void PrintList() {
        System.out.println(name + " ■ LISTE DE BOISSONS =============");
        for (Boisson b : listBoissons) {
            System.out.println(b.getPrix()+ "\t -- " + b.getNom() );
        }
    }

    public void SaveList(String cheminFichier) {
        StringBuilder sb = new StringBuilder();
        for (Boisson b : listBoissons) {
            sb.append(b.getNom() + ";" + (b.getPrix() + 1) + "\n");
        }
        CSVFileManager.EnregistreCSV(cheminFichier, sb.toString());
    }

    public void LoadList(String cheminFichier) {
        ArrayList<String> lines = CSVFileManager.ChargeCSV(cheminFichier);
        if (listBoissons.size() != 0)
            listBoissons.clear();

        for (String l : lines) {
            if (!l.isEmpty()) {
                String[] arr = l.split(";");
                // check NumberFormatException ???
                listBoissons.add(new Boisson(arr[0], Integer.parseInt(arr[1])));
            }
        }
    }

}
