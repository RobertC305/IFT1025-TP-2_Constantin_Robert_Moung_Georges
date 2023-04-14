module com {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.Main to javafx.fxml;
    opens server.models to javafx.base;
    exports com.Main;
}