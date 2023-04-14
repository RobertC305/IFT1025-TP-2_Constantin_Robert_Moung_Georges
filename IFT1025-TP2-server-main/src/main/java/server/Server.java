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

