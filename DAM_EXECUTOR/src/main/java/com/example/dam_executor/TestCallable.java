package com.example.dam_executor;

import java.util.concurrent.*;

public class TestCallable {
    public static void main() {
        Integer resultat;
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> result = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 10;
            }
        });
        executor.shutdown();

        long debut = System.currentTimeMillis();
        while (result.isDone()) {
            System.out.printf("attente (%d ms)%n", System.currentTimeMillis() - debut);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            resultat = result.get();
            System.out.printf("Resultat %d (%d ms)%n", resultat, System.currentTimeMillis() - debut);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
