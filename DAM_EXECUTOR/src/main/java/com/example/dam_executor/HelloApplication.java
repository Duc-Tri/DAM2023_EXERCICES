package com.example.dam_executor;

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

    // https://www.jmdoudoux.fr/java/dej/chap-executor.htm
    public static void main(String[] args) {

        int nbProcessorss = Runtime.getRuntime().availableProcessors();
        System.out.println("processeurs: " + nbProcessorss);

        TestCompletionService.main();

        //=========================================================================================
        launch();
    }
}