/**
 * Charge des images des Rover (Mars) du site de la NASA
 *
 * @author Tri - 2023
 */
package com.dam.dam_nasa;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NASAImagesURL {

    // API KEY obtenu après inscription, on est limité à 1000 images / heure (je crois ...)
    private static final String siteURL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=1000&api_key=Jjxe30UcSTWTEC3zdimgQ6MeDef1aZoAa1n9Gbkp";

    // l'indice de l'image actuellement affichée
    private static int imageIndice;

    // tableau qui contient les URLs des images
    public static ArrayList<String> imagesURL;

    /**
     * Charge les URLs des images à partir du JSON de la NASA
     *
     * @param imageView
     */
    public static String loadImages(ImageView imageView) {

        imageIndice = 0;
        StringBuilder fullText = new StringBuilder();

        // READ FROM NASA ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
        try {
            URL oracle = new URL(siteURL);

            System.out.println(oracle.getDefaultPort() + " testConnection .......... " + siteURL);

            BufferedReader reader = new BufferedReader(new InputStreamReader(oracle.openStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                fullText.append(line);
            }
            reader.close();

            //System.out.println(fullText);

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException");
        } catch (IOException e) {
            System.out.println("IOException");
        }

        // PARSE JSON ■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■
        imagesURL = new ArrayList<>();

        if (!fullText.isEmpty()) {

            JSONObject jo = new JSONObject(String.valueOf(fullText));

            // on a un array "photos" à la racine
            JSONArray arr = jo.getJSONArray("photos");
            for (int i = arr.length() - 1; i >= 0; i--) {
                JSONObject photo = arr.getJSONObject(i);

                // on ne garde que des photos issues de la caméra MASTER
                if (photo.getJSONObject("camera").getString("name").equals("MAST")) {

                    imagesURL.add(photo.getString("img_src").replace("http://","https://"));
                }
            }

            // si l'on a chargé les images, j'affiche l'image au milieu du lot
            //imageIndice = imagesURL.size() / 2;

            // ... à moins que je préfère une image au hasard ...
            imageIndice = (int) (Math.random() * imagesURL.size());

            return showCurrentImage(imageView);
        }

        return "ERROR !";
    }

    /**
     * Affiche l'image précédente
     *
     * @param imageView container
     * @return numéro de l'image / nombre d'images
     */
    public static String previousImage(ImageView imageView) {
        imageIndice--;
        if (imageIndice < 0)
            imageIndice = 0;

        return showCurrentImage(imageView);
    }

    /**
     * Affiche l'image suivante
     *
     * @param imageView container
     * @return numéro de l'image / nombre d'images
     */
    public static String nextImage(ImageView imageView) {
        imageIndice++;
        if (imageIndice > imagesURL.size() - 1)
            imageIndice = imagesURL.size() - 1;

        return showCurrentImage(imageView);
    }

    /**
     * Met l'image courante dans un ImageView
     *
     * @param imageView container
     * @return
     */
    private static String showCurrentImage(ImageView imageView) {

        if (!imagesURL.isEmpty() && imageIndice >= 0 && imageIndice < imagesURL.size()) {
            try {
                // System.out.println(">>" + imagesURL.get(imageIndice) + "<<");

                // les URLs contiennent "http://" alors qu'il faudrait les convertir en "https://"
                String imageUrl = imagesURL.get(imageIndice); //.replaceAll("http://", "https://");

                Image im = new Image(imageUrl);
                imageView.setImage(im);
                imageView.setFitHeight(500);
                imageView.setFitWidth(500);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return imageIndice + " / " + imagesURL.size();
    }

}
