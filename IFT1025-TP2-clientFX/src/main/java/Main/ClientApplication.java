package Main;

import Controller.Controller;
import server.models.Client;
import View.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * C'est la classe Main de l'application.
 */
public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        View view = new View();
        Client client = new Client();
        Controller controller = new Controller(client,view);

        Scene scene = new Scene(view, 600, 400);

        stage.setScene(scene);
        stage.setTitle("Application Client");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}