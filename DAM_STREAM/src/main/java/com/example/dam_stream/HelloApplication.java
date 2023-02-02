package com.example.dam_stream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        BoissonsList boissonsList =  new BoissonsList("ma liste de boissons");
        boissonsList.GenerateList();
        boissonsList.PrintList();

        boissonsList.SaveList("mes_boissons1.csv");
        boissonsList.LoadList("mes_boissons1.csv");
        boissonsList.PrintList();

        boissonsList.SaveList("mes_boissons2.csv");
        boissonsList.LoadList("mes_boissons2.csv");
        boissonsList.PrintList();

        BufferedReader inputStream = null;
        try {
            inputStream = new BufferedReader(new FileReader(HelloApplication.class.getResource("/TestHello").getPath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(inputStream.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        //launch();
    }

}