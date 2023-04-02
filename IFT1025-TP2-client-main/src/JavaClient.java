import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class JavaClient {

    public static void main(String[] args) {

        try {
            Socket client = new Socket("127.0.0.1",1337);
            //ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());

            //objectInputStream = new OutputStreamWriter(cS.getOutputStream());

            //BufferedWriter bw = new BufferedWriter(os);

            Scanner scanner = new Scanner(System.in);

            while(scanner.hasNext()) {
                String line = scanner.nextLine();
                System.out.println("J'ai envoyé "+line );
                objectOutputStream.writeObject(line);
                objectOutputStream.flush();
                //bw.append(line+"\n");
                //bw.flush();
                if(line.equals("exit")) {
                    System.out.println("Au revoir.");
                    break;
                }
            }

            objectOutputStream.close();
            //bw.close();
            scanner.close();
            client.close();

        } catch (ConnectException x) {
            System.out.println("Connexion impossible sur port 1337: pas de serveur.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
