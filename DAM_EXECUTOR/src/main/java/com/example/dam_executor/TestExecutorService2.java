package com.example.dam_executor;

import java.util.concurrent.*;

public class TestExecutorService2 {

    public static void main() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Future<String> future1 = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int i = 0;
                System.out.println("DEBUT TACHE 1");
                while (i < 10 && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                    i++;
                }
                System.out.println("FIN TACHE 1");
                return "TACHE 1";
            }
        });


        Future<String> future2 = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int i = 0;
                System.out.println("DEBUT TACHE 2");
                while (i < 10 && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(500);
                    i++;
                }
                System.out.println("FIN TACHE 2");
                return "TACHE 2";
            }
        });

        executorService.shutdown();

        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
            System.out.println("result 1 = " + future1.get());
            System.out.println("result 2 = " + future2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
