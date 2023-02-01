package com.example.dam_executor;

import java.util.concurrent.*;

public class TestExecutorService1 {

    public static void main() {
        ExecutorService executorService = new ThreadPoolExecutor(2, 4, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

        for (int i = 0; i < 50; i++) {
            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    int rand = 1000 + (int)(Math.random() * 10000);
                    System.out.println(rand+ " ► debut tâche " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(rand);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(rand+ " ► fin tâche / " + Thread.currentThread().getName());
                }
            });

            /*
        try {
            System.out.println("resultat=" + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

 */
        }

        System.out.println("Autre Traitement");


        executorService.shutdown();
        try {
            executorService.awaitTermination(300, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fin du thread principal");

    }


}
