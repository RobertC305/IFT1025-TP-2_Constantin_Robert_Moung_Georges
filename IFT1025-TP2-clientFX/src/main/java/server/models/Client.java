package server.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private Socket client;
    private static int port = 1337;
    private static String ipAdress = "127.0.0.1";

    private static ArrayList<Course> listCoursesConsulted = new ArrayList<>();


    /**
     * Constructeur
     */
    public Client() {
            this.client = new Socket();
    }


    /**
     * @return tableau qui contient la liste de cours de la session choisie.
     */
    public ArrayList<Course> getListCoursesConsulted() {
        return listCoursesConsulted;
    }

    /**
     * Cette méthode prend le premier élément de la ligne de commande avant un espace(" ") et le retourne
     * @param line Ligne de commande pour laquelle la méthode prends le premier élément
     * @return Le premier élément avant un espace(" ") sous forme de String
     */
    public static String commande(String line) {
        String[] instructions = line.split(" ");
        String commande = instructions[0];
        return commande;
    }

    /**
     * La commande suivante test si la commande entrée en paramètre est valide selon les valeurs acceptées. Si c'est le cas,
     * aucune action n'est effectuée, sinon une nouvelle commande est demandée tant que c'est invalide,
     * @param commande Commande à tester avec les valeurs acceptées
     * @param acceptedValues Liste des valeurs acceptées sous forme String[]
     * @param scanner Scanner auquel redemander une commande, si nécessaire
     * @return Retourne la commande initiale si elle est valide, sinon, retourne la prochaine commande valide entrée dans le scanner.
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
     * @param email Email a tester
     * @return "true" si valide, "false" sinon
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
     * @param matricule matricule à tester (String)
     * @return "true" si valide, "false" sinon
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
     * @param codeToValidate Code à valider (String)
     * @param CourseList Liste de cours valides (ArrayList<Course>)
     * @return "true" si valide, "false" sinon
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
     * Cette méthode se connecte au serveur localhost sur le port 1337, envoie la commande "CHARGER session" au serveur,
     * àaffiche les cours disponibles pour la session reçu du serveur et se déconnecte par la suite.
     * @param session (1=Automne, 2=Hiver, 3 = Ete)
     * @throws IOException
     */
    public static void charger(String session) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("127.0.0.1", 1337);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream.writeObject("CHARGER "+session);

        ArrayList<Course> filteredCourses = (ArrayList<Course>) objectInputStream.readObject();
        listCoursesConsulted.clear();
        listCoursesConsulted.addAll(filteredCourses);
        objectOutputStream.flush();

        objectOutputStream.close();
        objectInputStream.close();
    }

    /**
     * Cette methode envoie un objet RegistrationForm au serveur localhost sur le port 1337. Il se connecte au serveur.
     * envoie la commande et se déconnecte.
     * @param registrationForm Objet de format RegistrationForm à envoyer au serveur.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static void inscrire(RegistrationForm registrationForm) throws IOException, ClassNotFoundException {
        Socket socket = new Socket(ipAdress, port);
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
