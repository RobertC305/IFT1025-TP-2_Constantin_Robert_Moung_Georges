package Main;

import Controller.Controller;
import Model.Client;
import Model.Course;
import View.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

package main.java.ClientApplication;
public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        View view = new View();
        //Client client = new Client();
        //Course course = new Course();
        //Controller controller = new Controller();

        Scene scene = new Scene(view, 500, 600);

        stage.setScene(scene);
        stage.setTitle("Application Client");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}