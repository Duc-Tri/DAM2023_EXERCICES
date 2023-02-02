module com.example.dam_stream {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    /*
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

     */

    opens com.example.dam_stream to javafx.fxml;
    exports com.example.dam_stream;
}