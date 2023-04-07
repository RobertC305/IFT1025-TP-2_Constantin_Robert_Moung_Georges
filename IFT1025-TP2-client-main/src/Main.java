import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            Client client = new Client(new Socket("127.0.0.1", 1337));
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            Scanner scanner = new Scanner(System.in);
            messageChoixCours();


            String line = scanner.nextLine();
            ifExitDisconnect(line,client);

            //L'utilisateur peut rentrer un nombre de 1 à 3 pour afficher les cours
            //String[] instructions = line.split(" ");
            //String commande = instructions[0];
            String commande=commande(line);



            if (commande.equals("1") | commande.equals("2") | commande.equals("3"))
            {
                client.openStream();
                client.charger(commande);
                messageChoixApresCharger();

                line = scanner.nextLine();
                ifExitDisconnect(line,client);
                //instructions = line.split(" ");
                //commande = instructions[0];
                commande=commande(line);


                //Changer pour un while pour que la boucle soit lue tant que l'option 1 est choisie
                if (commande.equals("1")) {
                    while (true) {
                        messageChoixCours();
                        line = scanner.nextLine();
                        ifExitDisconnect(line, client);
                        //instructions = line.split(" ");
                        //commande = instructions[0];
                        commande=commande(line);

                        client.charger(commande);
                        messageChoixApresCharger();
                        line = scanner.nextLine();
                        ifExitDisconnect(line, client);
                        //instructions = line.split(" ");
                        //commande = instructions[0];
                        commande=commande(line);

                        if (commande.equals("1") != true) {
                            break;
                        }

                    }
                }
                if (commande.equals("2")){
                    //System.out.println(client.getListCoursesConsulted());
                    System.out.print("Veuillez saisir votre prénom: ");
                    String prenom = scanner.nextLine();
                    System.out.print("Veuillez saisir votre nom: ");
                    String nom = scanner.nextLine();
                    System.out.print("Veuillez saisir votre email: ");
                    String email = scanner.nextLine();
                    System.out.print("Veuillez saisir votre matricule: ");
                    String matricule = scanner.nextLine();
                    System.out.print("Veuillez saisir le code du cours: ");
                    String code = scanner.nextLine();
                    Course coursInscription = null;
                    boolean coursValide = false;
                    for (Course course: client.getListCoursesConsulted()) {
                        if (course.getCode().equals(code)){
                            coursInscription = course;
                            coursValide = true;
                        }
                    }
                    if (coursValide==false){
                        System.out.println("Cours invalide");
                    }
                    //Deconnection du client apres la première commande
                    //client.disconnect();

                    //Reconnection du client pour la deuxième commande
                    RegistrationForm registrationForm = new RegistrationForm(prenom,nom,email,matricule,coursInscription);
                    client.inscrire(registrationForm);
                }
            } else {
                System.out.println("Invalide, veuillez sélectionner un nombre entre 1 et 3");

            }


            scanner.close();


        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Cette méthode se déconnecte du client en paramètre et quitte le programme java si la ligne en paramètre est
     * égale à "exit".
     * @param line Line à examiner
     * @param client Le client duquel il faut se déconnecter
     * @throws IOException
     */
        public static void ifExitDisconnect(String line, Client client) throws IOException {
            if (line.equals("exit")) {
                System.out.println("Au revoir.");
                client.disconnect();
                System.exit(0);
            }
        }

        public static String commande(String line) {
            String[] instructions = line.split(" ");
            String commande = instructions[0];
            return commande;
        }


    /**
     * La méthode suivante imprime un texte prédéfini pour afficher les sessions
     */
    public static void messageChoixCours(){
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        System.out.println("1. Automne");
        System.out.println("2. Hiver");
        System.out.println("3. Ete");
        System.out.print("Choix: ");

    }

    /**
     * La méthode suivante imprime un texte prédéfini pour afficher les choix de commandes après avoir affiché les
     * cours disponibles dans une session
     */
    public static void messageChoixApresCharger(){
        System.out.println("Choix:");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.println("2. Inscription à un cours");
    }
}
