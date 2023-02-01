package com.example.dam_executor;

import java.util.*;
import java.util.concurrent.*;

public class TestExecutorService3 {
    public static void main() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Set<Callable<String>> callables = new HashSet<Callable<String>>();


        callables.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int i = 0;
                System.out.println("DEBUT TACHE 1...........");
                while (i < 100 && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(10000);
                    i++;
                }
                System.out.println("...............FIN TACHE 1");
                return "TACHE#1";
            }
        });


        callables.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int i = 0;
                System.out.println("DEBUT TACHE 2...........");
                while (i < 50 && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(200);
                    i++;
                }
                System.out.println("...............FIN TACHE 2");
                return "TACHE#2";
            }
        });


        callables.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                int i = 0;
                System.out.println("DEBUT TACHE 3...........");
                while (i < 20 && !Thread.currentThread().isInterrupted()) {
                    Thread.sleep(100);
                    i++;
                }
                System.out.println("...............FIN TACHE 3");
                return "TACHE#3";
            }
        });


        try {
            String result = executorService.invokeAny(callables);
            System.out.println("RESULT ============ " + result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();

    }
}
