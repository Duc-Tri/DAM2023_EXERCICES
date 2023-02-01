package com.example.dam_executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TestCompletionService {
    private static final int NB_TACHES = 5;

    public static void main() {
        ExecutorService executor = Executors.newFixedThreadPool(NB_TACHES);
        CompletionService<Integer> completion = new ExecutorCompletionService<Integer>(executor);

        for (int i = 0; i < NB_TACHES; i++) {
            completion.submit(new MaTache(NB_TACHES - 1));
        }


        for(int i=0;i<NB_TACHES;i++)
        {
            Integer resultat;
            try {
                resultat = completion.take().get();
                System.out.println("resultat.............." + resultat);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
