import server.models.Course;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    /**
     * Constructeur
     * @param client Socket pour une connection au seveur
     */
    public Client(Socket client) {
        this.client = client;

    }

    /**
     * Création des ObjectOutputStream et ObjectInputStream
     * @throws IOException
     */
    public void openStream() throws IOException {
        this.objectOutputStream = new ObjectOutputStream(client.getOutputStream());
        this.objectInputStream = new ObjectInputStream(client.getInputStream());
    }

    /**
     * Fermeture des ObjectOutputStream et ObjectInputStream
     * @throws IOException
     */
    public void closeStream() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();

    }

    /**
     * Envoie la commande "CHARGER session" au serveur et affiche les cours disponibles pour la session.
     * @param numSession (1=Automne, 2=Hiver, 3 = Ete)
     * @throws IOException
     */
    public void charger(int numSession) throws IOException, ClassNotFoundException {
        this.openStream();
        System.out.println("J'ai envoyé "+"Ete" );
        objectOutputStream.writeObject("CHARGER Ete");
        ArrayList<Course> objectReceived = (ArrayList<Course>) objectInputStream.readObject();
        System.out.println(objectReceived);
        System.out.println(objectReceived.get(0));
        objectOutputStream.flush();

    }

}
