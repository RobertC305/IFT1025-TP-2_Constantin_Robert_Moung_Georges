package server.models;

import java.io.Serializable;

/**
 * Cours
 */
public class Course implements Serializable {

    /**
     * Nom du cours
     */
    private String name;

    /**
     * Code du cours
     */
    private String code;

    /**
     * Session dans laquelle se donne le cours
     */
    private String session;

    /**
     * Constructeur
     * @param name nom du cours
     * @param code code du cours
     * @param session session du cours
     */
    public Course(String name, String code, String session) {
        this.name = name;
        this.code = code;
        this.session = session;
    }

    /**
     * Getter du nom du cours
     * @return nom du cours
     */
    public String getName() {
        return name;
    }

    /**
     * Setter du nom du cours
     * @param name Nouveau nom du cours à assigner
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter du code du cours
     * @return code du cours
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter du code du cours
     * @param code Nouveau code du cours à assigner
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter de la session du cours
     * @return Session du cours
     */
    public String getSession() {
        return session;
    }

    /**
     * Setter de la session du cours
     * @param session session du cours
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * Cette méthode établit le format que doit avoir le cours lorsqu'il est affiché sous une forme String.
     * @return Le cours sous forme String
     */
    @Override
    public String toString() {
        return "Course{" +
                "name=" + name +
                ", code=" + code +
                ", session=" + session +
                '}';
    }
}
