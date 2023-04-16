package server.models;

import java.io.Serializable;

/**
 * Classe du formulaire d'inscription.
 */
public class RegistrationForm implements Serializable {

    /**
     * Prénom de l'étudiant.
     */
    private String prenom;

    /**
     * Nom de l'étudiant.
     */
    private String nom;

    /**
     * Email de l'étudiant.
     */
    private String email;

    /**
     * Matricule de l'étudiant.
     */
    private String matricule;

    /**
     * Cours auquel l'étudiant s'inscrit.
     */
    private Course course;

    /**
     * Constructeur
     * @param prenom Prénom de l'étudiant
     * @param nom Nom de l'étudiant
     * @param email Email de l'étudiant
     * @param matricule Matricule de l'étudiant
     * @param course Cours auquel l'étudiant s'inscrit
     */
    public RegistrationForm(String prenom, String nom, String email, String matricule, Course course) {
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.course = course;
    }

    /**
     * Getter du prénom de l'étudiant
     * @return Prénom de l'étudiant
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Setter du prénom de l'étudiant
     * @param prenom Nouveau prénom de l'étudiant
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Getter du nom de l'étudiant
     * @return Nom de l'étudiant
     */
    public String getNom() {
        return nom;
    }

    /**
     * Setter du nom de l'étudiant
     * @param nom  Nouveau nom de l'étudiant
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Getter de l'email de l'étudiant
     * @return Email de l'étudiant
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter de l'email de l'étudiant
     * @param email Nouvel email de l'étudiant
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter de la matricule de l'étudiant
     * @return Matricule de l'étudiant
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * Setter de la matricule de l'étudiant
     * @param matricule Nouvelle matricule de l'étudiant
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * Getter du cours
     * @return Cours
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Setter du cours
     * @param course nouveau cours
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Cette méthode établit le format que doit avoir le formulaire d'inscription lorsqu'il est affiché
     * sous une forme String.
     * @return Le formulaire sous forme String
     */
    @Override
    public String toString() {
        return "InscriptionForm{" + "prenom='" + prenom + '\'' + ", nom='" + nom + '\'' + ", email='" + email + '\'' + ", matricule='" + matricule + '\'' + ", course='" + course + '\'' + '}';
    }
}
