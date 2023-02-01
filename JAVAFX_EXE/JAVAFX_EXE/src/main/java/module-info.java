module com.example.javafx_exe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.javafx_exe to javafx.fxml;
    exports com.example.javafx_exe;
}