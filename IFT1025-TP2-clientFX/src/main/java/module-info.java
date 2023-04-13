module com.example.ift1025tp2clientfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ift1025tp2clientfx to javafx.fxml;
    opens server.models to javafx.base;
    exports Main;
}