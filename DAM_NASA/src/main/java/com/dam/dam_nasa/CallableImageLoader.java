package com.dam.dam_nasa;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.concurrent.Callable;

public class CallableImageLoader implements Callable<Integer> {
    public Image image;
    private String imageURL;
    int num;
    ImageView imageView;

    public CallableImageLoader(ImageView iv, String url, int n) {
        imageView = iv;
        imageURL = url;
        num = n;
    }

    @Override
    public Integer call() throws Exception {
        //monImage = null;
        image = new Image(imageURL,true);
        imageView.setImage(image);
        System.out.println("chargement......" + Thread.currentThread().getName() + " - " + imageURL);

        return num;
    }


}
