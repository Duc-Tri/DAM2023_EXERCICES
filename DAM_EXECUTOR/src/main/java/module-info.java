module com.example.dam_executor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.dam_executor to javafx.fxml;
    exports com.example.dam_executor;
}