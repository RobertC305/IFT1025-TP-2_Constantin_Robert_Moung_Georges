import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {

        try {
            Client client = new Client(new Socket("127.0.0.1", 1337));
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            Scanner scanner = new Scanner(System.in);
            messageChoixCours();


            String line = scanner.nextLine();
            //ifExitDisconnect(line,client);

            //L'utilisateur peut rentrer un nombre de 1 à 3 pour afficher les cours
            //String[] instructions = line.split(" ");
            //String commande = instructions[0];
            //String commande=commande(line);
            String commande = redemanderSiInvalide(commande(line), new String[]{"1", "2", "3","exit"},scanner);
            ifExitDisconnect(line,client);

            if (commande.equals("1") | commande.equals("2") | commande.equals("3"))
            {
                client.openStream();
                client.charger(commande);
                messageChoixApresCharger();

                line = scanner.nextLine();
                commande = redemanderSiInvalide(commande(line), new String[]{"1", "2","exit"},scanner);
                ifExitDisconnect(commande,client);

                //Changer pour un while pour que la boucle soit lue tant que l'option 1 est choisie
                if (commande.equals("1")) {
                    while (true) {
                        messageChoixCours();
                        line = scanner.nextLine();
                        commande = redemanderSiInvalide(commande(line), new String[]{"1", "2", "3","exit"},scanner);
                        ifExitDisconnect(commande, client);

                        client.charger(commande);
                        messageChoixApresCharger();
                        line = scanner.nextLine();
                        commande = redemanderSiInvalide(commande(line), new String[]{"1", "2","exit"},scanner);
                        ifExitDisconnect(commande, client);

                        if (commande.equals("1") != true) {
                            break;
                        }

                    }
                }
                if (commande.equals("2")){
                    //System.out.println(client.getListCoursesConsulted());
                    System.out.print("Veuillez saisir votre prénom: ");
                    String prenom = scanner.nextLine();
                    ifExitDisconnect(prenom, client);

                    System.out.print("Veuillez saisir votre nom: ");
                    String nom = scanner.nextLine();
                    ifExitDisconnect(nom, client);

                    System.out.print("Veuillez saisir votre email: ");
                    String email = scanner.nextLine();
                    ifExitDisconnect(email, client);
                    //Vérifier si valide
                    while (emailValide(email) != true) {
                        System.out.println("Email invalide");
                        System.out.print("Veuillez saisir votre email: ");
                        email = scanner.nextLine();
                        ifExitDisconnect(email, client);
                    }

                    System.out.print("Veuillez saisir votre matricule: ");
                    String matricule = scanner.nextLine();
                    ifExitDisconnect(matricule, client);
                    //Vérifier si valide
                    while (matriculeValide(matricule) == false) {
                        System.out.println("Matricule invalide");
                        System.out.print("Veuillez saisir votre matricule: ");
                        matricule = scanner.nextLine();
                        ifExitDisconnect(matricule, client);
                    }

                    System.out.print("Veuillez saisir le code du cours: ");
                    String code = scanner.nextLine();
                    ifExitDisconnect(code, client);
                    //Vérifier si valide
                    while(coursCodeValide(code,client.getListCoursesConsulted()) != true){
                        System.out.println("Code invalide");
                        System.out.print("Veuillez saisir le code du cours: ");
                        code = scanner.nextLine();
                        ifExitDisconnect(code, client);
                    }
                    Course coursInscription = null;
                    for (Course course: client.getListCoursesConsulted()) {
                        if (course.getCode().equals(code)){
                            coursInscription = course;
                            break;
                        }
                    }

                    //Reconnection du client pour la deuxième commande
                    RegistrationForm registrationForm = new RegistrationForm(prenom,nom,email,matricule,coursInscription);
                    client.inscrire(registrationForm);
                }
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

    /**
     * Cette méthode prend le premier élément de la ligne de commande et le retounre
     * @param line Ligne de commande pour laquelle la méthode prends le premier élément
     * @return
     */
    public static String commande(String line) {
            String[] instructions = line.split(" ");
            String commande = instructions[0];
            return commande;
        }

    /**
     * La commande suivante test si la commande entrée en paramètre est valide selon les valeurs acceptées. Si c'est le cas,
     * aucune action n'est effectuée, sinon une nouvelle commande est demandée tant que c'est invalide,
     * @param commande Commande a tester
     * @param acceptedValues Liste des valeurs acceptées
     * @param scanner Scanner auquel redemander une commande, si nécéssaire
     * @return
     */
        public static String redemanderSiInvalide(String commande, String[] acceptedValues, Scanner scanner ){
            boolean invalide = true;
            while (invalide) {
                for (String value : acceptedValues) {
                    if (commande.equals(value) == true) {
                        invalide = false;
                        break;
                    }
                }
                if (invalide == false){
                    return commande;
                }
                System.out.println("Invalide, veuillez réessayer");
                commande = commande(scanner.nextLine());
            }
            return commande;
        }

    /**
     * La methode valide si l'email a un format valide.
     * @param email
     * @return
     */
    public static boolean emailValide(String email){
            //String regex = "\s+@\s+.\s+"; //Exemple prof
            String regex = "^(.+)@(.+)$"; //Lui il marche
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }

    /**
     * La methode valide si la matricule a un format valide.
     * @param matricule
     * @return
     */
    public static boolean matriculeValide(String matricule){
            try {
                int matriculeInt = Integer.parseInt(matricule);
                return true;
            } catch (NumberFormatException e){
                return false;
            }
        }

    /**
     * Cette méthode valide que le code fourni en paramètre est dans la liste de cours fournie en paramètre.
     * @param codeToValidate
     * @param CourseList
     * @return
     */
        public static boolean coursCodeValide(String codeToValidate, ArrayList<Course> CourseList){
            for (Course course: CourseList) {
                if (course.getCode().equals(codeToValidate)){
                    return true;
                }
            }
            return false;
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
        System.out.print("Choix: ");
    }
}
