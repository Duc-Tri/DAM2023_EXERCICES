package com.dam.dam_nasa;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

public class GridHandler {
    private static final int START_IMAGE = 600;
    TilePane grille;
    final Image spinner = new Image(String.valueOf(getClass().getResource("/rover.jpg")));
    static final int MAX_VIGNETTES = 100;
    static final int W_VIGNETTE = 65;
    static final int H_VIGNETTE = 50;
    static int currVignette = 0; // image courante (cf. NASAImagesLoader//imagesURL)

    static ImageView ivVignettes[] = new ImageView[MAX_VIGNETTES];

    public GridHandler(TilePane g) {
        grille = g;

        System.out.println(grille.widthProperty() + " / " + grille.heightProperty());

        // Put a spinner in all vignettes
        //=========================================================================================
        for (int x = 0; x < MAX_VIGNETTES; x++) {

            ImageView iv = new ImageView(spinner);

            //==============================================================================================
            // new ImageView(NASAImagesLoader.imagesURL.get(x));
            //new ImageView("https://cdn-s-www.leprogres.fr/images/8058EB07-4F4D-4995-9BD3-29A3DEF0DA96/NW_raw/photo-nasa-1647246390.jpg");
            /*
            if (x < 5) {
                CallableImageLoader c = new CallableImageLoader(NASAImagesURL.imagesURL.get(x), x);
                try {
                    c.call();
                    iv.setImage(c.image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

             */


            //==============================================================================================

            iv.setFitWidth(W_VIGNETTE);
            iv.setFitHeight(H_VIGNETTE);
            grille.getChildren().add(iv);

            ivVignettes[x] = iv;
        }

        ImagesCompletionService.Launch(ivVignettes, NASAImagesURL.imagesURL.subList(START_IMAGE, START_IMAGE + MAX_VIGNETTES));

        // Load a set of images
        //=========================================================================================
        /*
        for (int x = 0; x < MAX_VIGNETTES; x++) {
            // shift if needed
            /////////ImagesCompletionService.FutureImage(x, NASAImagesURL.imagesURL.get(x));
        }

         */
    }

    public static void displayImage(int index, Image image) {
        System.out.println("displayImage........" + index + "..." + image.getUrl());

        if (index < ivVignettes.length)
            ivVignettes[index].setImage(image);
    }

}

