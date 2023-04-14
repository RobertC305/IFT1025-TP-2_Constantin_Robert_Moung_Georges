package server;

/**
 * Interface fonctionnelle pour les méthodes qui répondent aux événements (event handlers)
 */
@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
