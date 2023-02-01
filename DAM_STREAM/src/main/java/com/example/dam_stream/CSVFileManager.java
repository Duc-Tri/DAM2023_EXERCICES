package com.example.dam_stream;

import java.io.*;
import java.util.ArrayList;

public class CSVFileManager {

    private static PrintWriter outputStream = null;
    private static BufferedReader inputStream = null;

    public static boolean EnregistreCSV(String cheminFichier, String CSVcontent) {

        try {
            outputStream = new PrintWriter(new FileWriter(cheminFichier));
            outputStream.println(CSVcontent);
        } catch (IOException e) {
            System.err.println("ERREUR sauvegarder: " + e.getMessage());
            return false;
        } finally {
            if (outputStream != null)
                outputStream.close();
        }

        return true;
    }

    public static ArrayList<String> ChargeCSV(String cheminFichier) {

        ArrayList<String> lines = new ArrayList<>();
        try {
            inputStream = new BufferedReader(new FileReader(cheminFichier));
            String line;

            while ((line = inputStream.readLine()) != null) {
                lines.add(line);
            }
            inputStream.close();

        } catch (FileNotFoundException e) {
            System.err.println("ERREUR Restaurer: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("ERREUR Restaurer: " + e.getMessage());
        }
        return lines;
    }


}
