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
        this.objectOutputStream.close();
        this.objectInputStream.close();

    }

    /**
     * Fermeture du client
     * @throws IOException
     */
    public void disconnect() throws IOException {
        client.close();
    }
    /**
     * Envoie la commande "CHARGER session" au serveur et affiche les cours disponibles pour la session.
     * @param numSession (1=Automne, 2=Hiver, 3 = Ete)
     * @throws IOException
     */
    public void charger(String numSession) throws IOException, ClassNotFoundException {
        String session;
        if (numSession.equals("1")) {
            session = "Automne";
            objectOutputStream.writeObject("CHARGER "+session);
            //System.out.println("J'ai envoye CHARGER "+session);
        } else if (numSession.equals("2")) {
            session = "Hiver";
            objectOutputStream.writeObject("CHARGER "+session);
            //System.out.println("J'ai envoye CHARGER "+session);
        } else if (numSession.equals("3")) {
            session = "Ete";
            objectOutputStream.writeObject("CHARGER "+session);
            //System.out.println("J'ai envoye CHARGER "+session);
        } else {
                IllegalArgumentException exception;
                exception = new IllegalArgumentException("Le numéro de session doit être entre 1 ou 3 ");
                throw exception;
        }

        ArrayList<Course> filteredCourses = (ArrayList<Course>) objectInputStream.readObject();

        System.out.println("Les cours offert pendant la session d'"+session.toLowerCase()+" sont:");

        int i=1; //Compteur de cours
        for (Course course : filteredCourses){
            System.out.println(i+". "+course.getCode()+"\t"+course.getName());
            i++;
        }
        objectOutputStream.flush();

    }

}
