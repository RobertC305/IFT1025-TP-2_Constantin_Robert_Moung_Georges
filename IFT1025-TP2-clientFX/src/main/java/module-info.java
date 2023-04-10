module com.example.ift1025tp2clientfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ift1025tp2clientfx to javafx.fxml;
    exports com.example.ift1025tp2clientfx;
}