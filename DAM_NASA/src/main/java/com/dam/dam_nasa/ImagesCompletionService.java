package com.dam.dam_nasa;

import javafx.scene.image.ImageView;

import java.util.List;
import java.util.concurrent.*;

public class ImagesCompletionService {
    static private ImageView[] ivVignettes;
    static private int total = 0;
    static final int MAX_THREADS = 50;
    static ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS); // pool
    static CompletionService<Integer> completion = new ExecutorCompletionService<Integer>(executor);

    static public void Launch(ImageView[] iv, List<String> imagesURL) {
        ivVignettes = iv;
        total = 0;
        ImageView oneImageView;
        for (int i = 0; i < imagesURL.size(); i++) {
            oneImageView = ivVignettes[i];
            String url = imagesURL.get(i);

            //System.out.println("ImagesCompletionService::Launch ----------------- " + url);

            completion.submit(new CallableImageLoader(oneImageView, url, total++));
        }

        for (int i = 0; i < total - 1; i++) {
            try {
                int num = completion.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        /////////////executor.shutdown();

        System.out.println("ImagesCompletionService::Launch IMAGES = " + imagesURL.size());
    }


    public static void FutureImageXXXXXXX(int index, String url) {

        System.out.println("__________ " + index + " = " + url);


        /////////CallableImageLoader task = new CallableImageLoader(url);
        //setCallable.add(task);

        /**
         Future<Image> future = esPool.submit(task);
         futureList.set(index, future);

         if (!running) {
         runMe();
         }
         */
    }

    static boolean running = false;

    private static void runMe() {
        if (running)
            return;

        running = true;
        int nbNulls = 0;
        /*
        for (int i = 0; i < futureList.size(); i++) {

            Future<Image> future = futureList.get(i);
            if (future == null) {
                nbNulls++;
                continue;
            }

            try {
                Image im = future.get();

                System.out.println("===== future " + im);

                if (im != null) {
                    GridHandler.displayImage(i, im);
                    futureList.remove(i);
                    nbNulls++;
                }
                Thread.sleep(500);

            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } catch (ExecutionException e) {
                System.err.println(e.getMessage());
            }
        }

        if (nbNulls == futureList.size()) {
            executor.shutdown();
            running = false;
            System.err.println(".................... SHUTDOWN !");

        }

         */
    }


}
