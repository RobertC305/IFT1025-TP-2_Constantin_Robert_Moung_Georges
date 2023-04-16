package server;

/**
 * Classe qui contient la méthode Main et démarre un serveur sur un port défini.
 */
public class ServerLauncher {
    /**
     * Port sur lequel le serveur sera créé.
     */
    public final static int PORT = 1337;

    /**
     * La méthode main crée une instance de serveur sur le port 1337 et fait appel à la méthode run du serveur.
     * @param args Aucun argument n'est utilisé.
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}