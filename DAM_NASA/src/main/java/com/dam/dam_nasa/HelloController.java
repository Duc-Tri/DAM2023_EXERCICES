package com.dam.dam_nasa;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button prevImageButton;
    @FXML
    private Button prevSetButton;
    @FXML
    private Button nextSetButton;
    @FXML
    private Button nextImageButton;


    @FXML
    private Label labelText;

    @FXML
    private ImageView imageView;

    @FXML
    private TilePane grille;

    @FXML
    protected void onHelloButtonClick() {
        labelText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onPreviousImageButton() {
        labelText.setText(NASAImagesURL.previousImage(imageView));
    }

    @FXML
    protected void onNextImageButton() {
        labelText.setText(NASAImagesURL.nextImage(imageView));
    }

    @FXML
    protected void on50PreviousImagesButton() {
        labelText.setText(NASAImagesURL.previousImage(imageView));
    }

    @FXML
    protected void on50NextImagesButton() {
        labelText.setText(NASAImagesURL.nextImage(imageView));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        labelText.setText(NASAImagesURL.loadImages(imageView));


        prevSetButton.setText("<< " + GridHandler.MAX_VIGNETTES + " images préc.");
        nextSetButton.setText(GridHandler.MAX_VIGNETTES + " images suiv. >>");

        GridHandler gridHandler = new GridHandler(grille);

    }

}