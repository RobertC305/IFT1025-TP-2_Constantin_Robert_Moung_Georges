import server.models.Course;
import server.models.RegistrationForm;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Main {

    private static ArrayList<Course> listCoursesConsulted = new ArrayList<>();

    public static void main(String[] args) {

        try {
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            Scanner scanner = new Scanner(System.in);
            messageChoixCours();


            String line = scanner.nextLine();
            ifExitDisconnect(line);

            //L'utilisateur peut rentrer un nombre de 1 à 3 pour afficher les cours
            String commande = redemanderSiInvalide(commande(line), new String[]{"1", "2", "3","exit"},scanner);

            ifExitDisconnect(line);

            if (commande.equals("1") | commande.equals("2") | commande.equals("3"))
            {
                charger(commande);
                messageChoixApresCharger();

                line = scanner.nextLine();
                commande = redemanderSiInvalide(commande(line), new String[]{"1", "2","exit"},scanner);
                ifExitDisconnect(commande);

                //Changer pour un while pour que la boucle soit lue tant que l'option 1 est choisie
                if (commande.equals("1")) {
                    while (true) {
                        messageChoixCours();
                        line = scanner.nextLine();
                        commande = redemanderSiInvalide(commande(line), new String[]{"1", "2", "3","exit"},scanner);
                        ifExitDisconnect(commande);

                        //client.charger(commande);
                        charger(commande);


                        messageChoixApresCharger();
                        line = scanner.nextLine();
                        commande = redemanderSiInvalide(commande(line), new String[]{"1", "2","exit"},scanner);
                        ifExitDisconnect(commande);

                        if (commande.equals("1") != true) {
                            break;
                        }

                    }
                }
                if (commande.equals("2")){
                    System.out.print("Veuillez saisir votre prénom: ");
                    String prenom = scanner.nextLine();
                    ifExitDisconnect(prenom);

                    System.out.print("Veuillez saisir votre nom: ");
                    String nom = scanner.nextLine();
                    ifExitDisconnect(nom);

                    System.out.print("Veuillez saisir votre email: ");
                    String email = scanner.nextLine();
                    ifExitDisconnect(email);
                    //Vérifier si valide
                    while (emailValide(email) != true) {
                        System.out.println("Email invalide");
                        System.out.print("Veuillez saisir votre email: ");
                        email = scanner.nextLine();
                        ifExitDisconnect(email);
                    }

                    System.out.print("Veuillez saisir votre matricule: ");
                    String matricule = scanner.nextLine();
                    ifExitDisconnect(matricule);
                    //Vérifier si valide
                    while (matriculeValide(matricule) == false) {
                        System.out.println("Matricule invalide");
                        System.out.print("Veuillez saisir votre matricule: ");
                        matricule = scanner.nextLine();
                        ifExitDisconnect(matricule);
                    }

                    System.out.print("Veuillez saisir le code du cours: ");
                    String code = scanner.nextLine();
                    ifExitDisconnect(code);
                    //Vérifier si valide

                    while(coursCodeValide(code,listCoursesConsulted) != true){
                        System.out.println("Code invalide");
                        System.out.print("Veuillez saisir le code du cours: ");
                        code = scanner.nextLine();
                        ifExitDisconnect(code);


                    }

                    Course coursInscription = null;
                    for (Course course: listCoursesConsulted) {
                        if (course.getCode().equals(code)){
                            coursInscription = course;
                            break;
                        }
                    }

                    //Reconnection du client pour la deuxième commande

                    RegistrationForm registrationForm = new RegistrationForm(prenom,nom,email,matricule,coursInscription);
                    inscrire(registrationForm);

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
     * Cette méthode quitte le programme java si la ligne en paramètre est
     * égale à "exit".
     * @param line
     */
        public static void ifExitDisconnect(String line) {
            if (line.equals("exit")) {
                System.out.println("Au revoir.");
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

    /**
     * Envoie la commande "CHARGER session" au serveur et affiche les cours disponibles pour la session.
     * @param numSession (1=Automne, 2=Hiver, 3 = Ete)
     * @throws IOException
     */
    public static void charger(String numSession) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 1337);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

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
        listCoursesConsulted.addAll(filteredCourses);

        System.out.println("Les cours offert pendant la session d'"+session.toLowerCase()+" sont:");

        int i=1; //Compteur de cours
        for (Course course : filteredCourses){
            System.out.println(i+". "+course.getCode()+"\t"+course.getName());
            i++;
        }
        objectOutputStream.flush();

        objectOutputStream.close();
        objectInputStream.close();
    }

    /**
     * Cette methode envoie un objet RegistrationForm au serveur.
     * @param registrationForm
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void inscrire(RegistrationForm registrationForm) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 1337);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream.writeObject("INSCRIRE "+registrationForm);
        objectOutputStream.flush();
        objectOutputStream.writeObject(registrationForm);

        String confirmation = objectInputStream.readObject().toString();
        System.out.println(confirmation);
        objectOutputStream.close();
        objectInputStream.close();
    }

}
