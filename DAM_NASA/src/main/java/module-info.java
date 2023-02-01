module com.dam.dam_nasa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;
    requires java.desktop;

    opens com.dam.dam_nasa to javafx.fxml;
    exports com.dam.dam_nasa;
}