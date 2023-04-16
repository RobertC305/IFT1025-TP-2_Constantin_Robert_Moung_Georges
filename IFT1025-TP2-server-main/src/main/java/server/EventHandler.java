package server;

/**
 * Interface fonctionnelle pour les méthodes qui répondent aux événements (event handlers)
 */
@FunctionalInterface
public interface EventHandler {
    /**
     * Méthode à appeler lorsqu'une nouvelle commande arrive.
     * @param cmd String qui correspond à la commande reçue.
     * @param arg String qui correspond à l'argument reçu.
     */
    void handle(String cmd, String arg);
}
