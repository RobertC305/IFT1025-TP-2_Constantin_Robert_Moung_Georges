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

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

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

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

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

        //Affichage pour confirmer la reception de la commande
        System.out.println("J'ai recu l'instruction: CHARGER");
        System.out.println("Argument recu:"+ arg);

        //Lecture du document cours.txt
        //À noter que le package Scanner a été ajouté

        try {
            Scanner reader = new Scanner(new File(".\\src\\main\\java\\server\\data\\cours.txt"));
                ArrayList<Course> courses = new ArrayList<>();
                while (reader.hasNext()) {
                    String courseCode = reader.next();
                    String courseName = reader.next();
                    String session  = reader.next();
                    courses.add(new Course(courseName,courseCode,session));
                }
            reader.close();

                //Affichage des cours dans terminal serveur
            //System.out.println(courses);

            // Voir exemple du prof. Le serveur demande de spécifier la session. On pourrait donc y mettre une
            //alternative si le client ne spécifie pas d'argument

            //Filtrer selon la session mentionnée en argument
            ArrayList<Course> filteredCourses = new ArrayList<>();
            for (Course c: courses){
                if (c.getSession().equals(arg)){
                    filteredCourses.add(c);
                }
            }

            //Affichage du résultat
            //System.out.println(filteredCourses);

            //Envoyer le résultat au client avec un OutputStream
            System.out.println("J'ai envoyé "+filteredCourses );
            objectOutputStream.writeObject(filteredCourses);
            objectOutputStream.flush();

            //Attendre réponse si le client passe une nouvelle demande
            listen();

        } catch (FileNotFoundException e) {
            System.out.println("Fichier non trouvé");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        // TODO: implémenter cette méthode
        System.out.println("J'ai recu l'instruction: INSCRIRE");

        boolean registrationSucces = true;


        try {

            //Réception de l'objet de type RegistrationForm
            RegistrationForm registrationForm = (RegistrationForm) this.objectInputStream.readObject();

            //Enregistrement des données du RegistrationForm sur le fichier inscription.txt
            FileWriter fileWriter = new FileWriter(".\\src\\main\\java\\server\\data\\inscription.txt",true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            String registrationString = registrationForm.getCourse().getSession() + "\t" + registrationForm.getCourse().getCode() + "\t" +
                    registrationForm.getMatricule() + "\t" + registrationForm.getPrenom() + "\t" + registrationForm.getNom() + "\t" + registrationForm.getEmail()+"\n";
            writer.append(registrationString);
            writer.close();
            handleRegistrationSuccess(registrationForm);

        } catch (FileNotFoundException e){
            System.out.println("Fichier inscription.txt non trouvé");
            handleRegistrationFailure();
        } catch (IOException e) {
            System.out.println("Erreur lors de la réception ou de l'envoie de données stream");
            handleRegistrationFailure();
        } catch (ClassNotFoundException e) {
            System.out.println("Classe recu en stream est introuvable");
            handleRegistrationFailure();
        }
    }

    /**
     * La méthode envoie un message au client pour confirmer la réussite de l'inscription
     * @param registrationForm le formulaire d'inscription du client
     * @throws IOException
     */
    public void handleRegistrationSuccess(RegistrationForm registrationForm){
        try {
            objectOutputStream.writeObject("Félicitations! Inscription réussie de " + registrationForm.getPrenom() + " au cours " + registrationForm.getCourse().getCode());
            objectOutputStream.flush();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }


    /**
     * La méthode envoie un message au client pour l'informer de l'échec de son inscription
     * @throws IOException
     */
    public void handleRegistrationFailure(){
        try {
            objectOutputStream.writeObject("L'inscription a échoué. Veuillez réessayer ou nous contacter si le problème persiste.");
            objectOutputStream.flush();
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}

