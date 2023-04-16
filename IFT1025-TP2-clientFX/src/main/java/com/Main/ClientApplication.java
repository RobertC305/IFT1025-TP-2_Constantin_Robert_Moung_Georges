package com.Main;

import com.Controller.Controller;
import server.models.Client;
import com.View.View;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * C'est la classe Main de l'application.
 */
public class ClientApplication extends Application {

    /**
     * Méthode qui affiche l'inface graphique.
     * @param stage Stage de l'application.
     * @throws Exception
     */
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

    /**
     * Méthode Main de de la classe qui appelle la méthode "Stat" de la classe.
     * @param args Arguments reçus.
     */
    public static void main(String[] args) {
        launch(args);
    }

}