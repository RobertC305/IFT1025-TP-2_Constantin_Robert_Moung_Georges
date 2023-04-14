package server;

import javafx.util.Pair;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Classe d'un serveur
 */
public class Server {
    /**
     * Commande qui fait appel à l'événement d'inscription
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";

    /**
     * Commande qui fait appel à l'événement de chargement des cours
     */
    public final static String LOAD_COMMAND = "CHARGER";

    /**
     * Socket du serveur
     */
    private final ServerSocket server;

    /**
     * Socket client
     */
    private Socket client;

    /**
     * Stream d'input du client
     */
    private ObjectInputStream objectInputStream;

    /**
     * Stream d'output vers le client
     */
    private ObjectOutputStream objectOutputStream;

    /**
     * Liste des méthodes qui répondent aux événements du serveur ("event handlers")
     */
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur de la classe qui crée le serveur la machine locale (localhost) sur le port spécifié en
     * paramètre et initie la liste des "event handlers".
     * @param port Port sur lequel démarrer le serveur
     * @throws IOException
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Cette méthode ajoute un "event handler" à la liste des "event handlers" du serveur.
     * @param h Event handler à ajouter.
     */

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     *Cette méthode fait appel au handlers et leur envoie la commande et argument en paramètre.
     * @param cmd Commande
     * @param arg Arguments
     */

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * Cette méthode démarre le serveur, établit une communication lecture/écriture avec le client qui s'y connecte et
     * s'y déconnecte lorsque l'instruction a été complétée. Le serveur reste en attente d'une connection indéfiniment.
     */

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *Cette méthode lit un objet reçu, le sépare en commande et argument, et le renvoie aux "handlers"
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Cette méthode prend une ligne de commande (chaîne de caractères) dont le premier terme est une commande
     * et la sépare en une paire Commande/arguments.
     * @param line Ligne de commande
     * @return Paire commande/arguments sous forme
     */

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Cette méthode se déconnecte du client et ferme les Input/Output Stream.
     * @throws IOException
     */

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Cette méthode fait appel au "event handler" approprié en fonction de la commande reçue.
     * @param cmd Commande sous forme String
     * @param arg Arguments sous forme String
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transformer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        // TODO: implémenter cette méthode

        //Lecture du document cours.txt

        try {
            //On va supposer que notre fichier .jar sera dans le même dossier que nos fichiers .txt
            //Scanner reader = new Scanner(new File(".\\src\\main\\java\\server\\data\\cours.txt"));
            Scanner reader = new Scanner(new File("cours.txt"));
                ArrayList<Course> courses = new ArrayList<>();
                while (reader.hasNext()) {
                    String courseCode = reader.next();
                    String courseName = reader.next();
                    String session  = reader.next();
                    courses.add(new Course(courseName,courseCode,session));
                }
            reader.close();

            //Filtrer selon la session mentionnée en argument
            ArrayList<Course> filteredCourses = new ArrayList<>();
            for (Course c: courses){
                if (c.getSession().equals(arg)){
                    filteredCourses.add(c);
                }
            }

            //Envoyer le résultat au client avec un OutputStream
            objectOutputStream.writeObject(filteredCourses);
            objectOutputStream.flush();
            

        } catch (FileNotFoundException e) {
            System.out.println("Fichier cours.txt non trouvé");
        } catch (RuntimeException e) {
            //Aucune intervention requise, car le client s'est déconnecté. Il faut juste poursuivre la methode run()
        } catch (IOException e) {
            //Aucune intervention requise, car le client s'est déconnecté. Il faut juste poursuivre la methode run()
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
        boolean registrationSucces = true;

        try {
            //Réception de l'objet de type RegistrationForm
            RegistrationForm registrationForm = (RegistrationForm) this.objectInputStream.readObject();

            //Enregistrement des données du RegistrationForm sur le fichier inscription.txt
            //On va supposer que notre fichier .jar sera dans le même dossier que nos fichiers .txt
            //FileWriter fileWriter = new FileWriter(".\\src\\main\\java\\server\\data\\inscription.txt",true);
            FileWriter fileWriter = new FileWriter("inscription.txt",true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            String registrationString = registrationForm.getCourse().getSession() + "\t" + registrationForm.getCourse().getCode() + "\t" +
                    registrationForm.getMatricule() + "\t" + registrationForm.getPrenom() + "\t" + registrationForm.getNom() + "\t" + registrationForm.getEmail()+"\n";
            writer.append(registrationString);
            writer.close();

            //Succès de l'inscription
            objectOutputStream.writeObject("Félicitations! Inscription réussie de " + registrationForm.getPrenom() + " au cours " + registrationForm.getCourse().getCode());
            objectOutputStream.flush();

        } catch (FileNotFoundException e){
            System.out.println("Fichier inscription.txt non trouvé");
            try {
                objectOutputStream.writeObject("L'inscription a échoué. Veuillez réessayer ou nous contacter si le problème persiste.");
                objectOutputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        } catch (IOException e) {
            System.out.println("Erreur lors de la réception ou de l'envoie de données stream");
            try {
                objectOutputStream.writeObject("L'inscription a échoué. Veuillez réessayer ou nous contacter si le problème persiste.");
                objectOutputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Classe reçue en stream est introuvable");
            try {
                objectOutputStream.writeObject("L'inscription a échoué. Veuillez réessayer ou nous contacter si le problème persiste.");
                objectOutputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}

