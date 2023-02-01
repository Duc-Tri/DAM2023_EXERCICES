package com.example.dam_executor;

import java.util.concurrent.Callable;

public class MaTache implements Callable<Integer> {

    private final int duree;

    public MaTache(int duree) {
        this.duree = duree;
    }

    @Override
    public Integer call() throws Exception {
        System.out.println("=====Debut tache " + Thread.currentThread().getName());

        try {
            Thread.sleep(1000 * duree);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("=====Fin tache " + Thread.currentThread().getName());

        return duree;
    }
}
