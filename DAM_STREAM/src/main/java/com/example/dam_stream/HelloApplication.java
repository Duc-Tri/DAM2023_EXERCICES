package com.example.dam_stream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        launch();
    }

}