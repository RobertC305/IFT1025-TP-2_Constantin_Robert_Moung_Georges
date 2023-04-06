import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try {
            //Socket client = new Socket("127.0.0.1",1337);
            Client client = new Client(new Socket("127.0.0.1",1337));
            System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
            //ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            //ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            //client.openStream();




            Scanner scanner = new Scanner(System.in);
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter le liste des cours:");
            System.out.println("1. Automne");
            System.out.println("2. Hiver");
            System.out.println("3. Ete");
            System.out.print("Choix: ");

            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                if(line.equals("exit")) {
                    System.out.println("Au revoir.");
                    break;
                }

                //Si l'instruction est INSCRIRE, il faut envoyer un RegistrationForm.
                //Il serait pt pertinent de mettre une condition pour savoir ce qu'on envoie
                String[] instructions = line.split(" ");
                String commande = instructions[0];

                if (commande.equals("1")){
                    System.out.println("On envoie 1");
                    client.charger(1);
                } else if (commande.equals("2")) {

                } else if (commande.equals("3")) {

                } else {
                    System.out.println("Invalide, veuillez sélectionner un nombre entre 1 et 3");
                }

                /* //Le code ci-dessous marche bien pour l'envoi d'instruction
                if (commande.equals("CHARGER")){
                    System.out.println("J'ai envoyé "+line );
                    objectOutputStream.writeObject(line);

                    //On reçoit un arrayList d'éléments Course
                    ArrayList<Course> objectReceived = (ArrayList<Course>) objectInputStream.readObject();


                    System.out.println(objectReceived);

                    System.out.println(objectReceived.get(0));

                    objectOutputStream.flush();
                } else if (commande.equals("INSCRIRE")) {
                    System.out.println("Commande INSCRIRE à exécuter");
                } else {
                    System.out.println("Commande invalide, veuillez réessayer.");
                }*/
            }

            //objectOutputStream.close();
            //objectInputStream.close();
            scanner.close();
            //client.close();

        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}
