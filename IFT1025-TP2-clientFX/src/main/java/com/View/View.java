package com.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.models.Course;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Cette classe est la vue de notre application.
 */
public class View extends HBox {

	//Éléments du coté gauche de la scene (la liste de cours).
	/**
	 * Conteneur parent de la section liste de cours.
	 */
	private VBox courseVBox = new VBox(250); //Contient tous les éléments de la partie : liste de cours

	/**
	 * Titre de la section "liste de cours"
	 */
	private Text courseTitle = new Text("Liste des cours");

	/**
	 * Table qui affiche les cours de la session choisie.
	 */
	private TableView<Course> courseTable = new TableView();
	/**
	 * Colonne "code" de la table "courseTable".
	 */
	private TableColumn<Course, String> courseCodeTableCol = new TableColumn<Course, String>("Code");
	/**
	 * Colonne "cours" de la table "courseTable".
	 */
	private TableColumn<Course, String> courseNameTableCol = new TableColumn<Course, String>("Cours");
	/**
	 * Liste Observable pour la table "courseTable".
	 * Elle contient la liste de cours envoyer par le serveur.
	 */
	private ObservableList<Course> loadedCourses = FXCollections.observableArrayList();

	//Bas de page de la partie: liste de cours
	/**
	 * Conteneur enfant du conteneur "courseVBox".
	 * Il contient le comboBox pour le choix de la session et
	 * le bouton qui permet de charger la liste de cours de la session choisie.
	 */
	private HBox courseSessionHBox = new HBox();
	/**
	 * Permet de choisir la session.
	 */
	private ComboBox sessionComboBox = new ComboBox();
	/**
	 * Bouton pour charger la liste de cours de la session choisie.
	 */
	private  Button sessionConfirmationButton = new Button("charger");


	//Éléments du coté droit de la scene (Formulaire d'inscription).
	/**
	 * Conteneur parent de la section formulaire d'inscription.
	 */
	private VBox registrationVBox = new VBox(250);

	/**
	 * Titre du formulaire d'inscription
	 */
	private Text registrationTitle = new Text("Formulaire d'inscription");

	/**
	 * Conteneur enfant de registrationVBox qui contient
	 * les éléments à remplire dans le formulaire d'inscription.
	 */
	private VBox registrationFieldsVBox = new VBox(); //Contient les éléments du formulaire d'inscriptions

	//Prenom
	/**
	 * Conteneur du prénom.
	 */
	private HBox prenomHBOx = new HBox();
	/**
	 * Étiquette du prénom.
	 */
	private Label prenomLabel = new Label("Prénom");
	/**
	 * Champs de texte du prénom.
	 */
	private TextField prenomTextField = new TextField();

	//Nom
	/**
	 * Conteneur du nom.
	 */
	private HBox nomHBOx = new HBox();
	/**
	 * Étiquette du nom.
	 */
	private Label nomLabel = new Label("Nom");
	/**
	 * Champs de texte du nom.
	 */
	private TextField nomTextField = new TextField();

	//Email
	/**
	 * Conteneur de l'adresse courrielle.'
	 */
	private HBox emailHBOx = new HBox();
	/**
	 * Étiquette de l'adresse courielle.
	 */
	private Label emailLabel = new Label("Email");
	/**
	 * Champs de texte de l'adresse courrielle.
	 */
	private TextField emailTextField = new TextField();

	//Matricule
	/**
	 * Conteneur du matricule.
	 */
	private HBox matriculeHBOx = new HBox();
	/**
	 * Étiquette du matricule.
	 */
	private Label matriculeLabel = new Label("Matricule");
	/**
	 * Champs de texte du matricule.
	 */
	private TextField matriculeTextField = new TextField();

	//Bouton de confirmation
	/**
	 * Bouton pour envoyer le formulaire au serveur.
	 */
	private Button registrationConfirmationButton = new Button("envoyer");

	/**
	 * Police pour les titres de l'application.
	 */
	private Font titlesFont = Font.font("Verdana", FontWeight.BOLD, 15);


	/**
	 * Constructeur de la classe View.
	 * Affiche les éléments visuelle de l'application.
	 */
	public View() {

		/*
		 *Partie de gauche: liste de cours
		 *
		 */

		this.courseTitle.setFont(titlesFont);

		//Table liste de cours
		this.courseCodeTableCol.setCellValueFactory(new PropertyValueFactory<Course,String>("code"));
		this.courseNameTableCol.setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
		this.courseTable.getColumns().add(courseCodeTableCol);
		this.courseTable.getColumns().add(courseNameTableCol);
		this.courseTable.setItems(loadedCourses);

		//Choix de session
		this.sessionComboBox.getItems().addAll("Automne","Hiver","Ete");
		this.sessionComboBox.setValue("Session");

		//Ajout du comboBox de Session et du bouton session dans le même conteneur
		this.courseSessionHBox.getChildren().add(sessionComboBox);
		this.courseSessionHBox.getChildren().add(sessionConfirmationButton);
		this.courseSessionHBox.setAlignment(Pos.CENTER);
		this.courseSessionHBox.setSpacing(10);
		this.courseSessionHBox.setPadding(new Insets(0,0,10,0));

		//Ajout de tous les éléments de la partie "Liste de cours" dans son conteneur
		this.courseVBox.getChildren().add(courseTitle);
		this.courseVBox.getChildren().add(courseTable);
		this.courseVBox.getChildren().add(new Separator());
		this.courseVBox.getChildren().add(courseSessionHBox);
		this.courseVBox.setAlignment(Pos.TOP_CENTER);
		this.courseVBox.setSpacing(20);


		/*
		 *Partie de droite: Formulaire d'inscription
		 *
		 */

		this.registrationTitle.setFont(titlesFont);

		//Place les paires d'éléments "Label" et "TextField" du formulaire d'inscription dans un même conteneur
		this.prenomHBOx.getChildren().addAll(this.prenomLabel,this.prenomTextField);
		this.nomHBOx.getChildren().addAll(this.nomLabel,this.nomTextField);
		this.emailHBOx.getChildren().addAll(this.emailLabel,this.emailTextField);
		this.matriculeHBOx.getChildren().addAll(this.matriculeLabel,this.matriculeTextField);

		this.prenomHBOx.setAlignment(Pos.CENTER_RIGHT);
		this.prenomHBOx.setSpacing(15);
		this.nomHBOx.setAlignment(Pos.CENTER_RIGHT);
		this.nomHBOx.setSpacing(15);
		this.emailHBOx.setAlignment(Pos.CENTER_RIGHT);
		this.emailHBOx.setSpacing(15);
		this.matriculeHBOx.setAlignment(Pos.CENTER_RIGHT);
		this.matriculeHBOx.setSpacing(15);

		//Place tous les éléments du juste au dessus dans un même conteneur
		this.registrationFieldsVBox.getChildren().addAll(prenomHBOx,nomHBOx,emailHBOx,matriculeHBOx);
		this.registrationFieldsVBox.setAlignment(Pos.CENTER);
		this.registrationFieldsVBox.setSpacing(10);

		//Ajout de tous les éléments de la partie "Formulaire d'inscription" dans son conteneur
		this.registrationVBox.getChildren().add(registrationTitle);
		this.registrationVBox.getChildren().add(this.registrationFieldsVBox);
		this.registrationVBox.getChildren().add(registrationConfirmationButton);
		this.registrationVBox.setAlignment(Pos.TOP_CENTER);
		this.registrationVBox.setSpacing(20);


		//Réunie les éléments de la liste de cours et du formulaire d'inscription dans un même conteneur parent
		this.getChildren().add(courseVBox);
		this.getChildren().add(new Separator(Orientation.VERTICAL));
		this.getChildren().add(registrationVBox);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.setPadding(new Insets(5,0,0,0));

	}


	/*
	 *Getters pour les éléments de la "Liste de cours"
	 *
	 */

	/**
	 * @return TableView: retourne la table qui contient la liste de cours.
	 */
	public TableView getCourseTable(){return this.courseTable;}

	/**
	 * @return ComboBox: retourne le ComboBox qui contient la liste des sessions
	 */
	public ComboBox getSessionComboBox() {return sessionComboBox;}

	/**
	 * @return Button: retourne le bouton qui permet de charger les cours de la session choisie.
	 */
	public Button getSessionConfirmationButton() {return sessionConfirmationButton;}


	/*
	 *Getters pour les éléments de la "Liste de cours"
	 *
	 */

	/**
	 * @return TextField: retourne le champ de texte qui contient le prenom.
	 */
	public TextField getPrenomTextField() {return prenomTextField;}

	/**
	 * @return TextField: retourne le champ de texte qui contient le nom.
	 */
	public TextField getNomTextField() {return nomTextField;}

	/**
	 * @return TextField: retourne le champ de texte qui contient l'émail.
	 */
	public TextField getEmailTextField() {return emailTextField;}

	/**
	 * @return TextField: retourne le champ de texte qui contient le matricule.
	 */
	public TextField getMatriculeTextField() {return matriculeTextField;}

	/**
	 * @return Button: retourne le bouton qui envoie les données rentrées au serveur afin de s'inscrire.
	 */
	public Button getRegistrationConfirmationButton() {return registrationConfirmationButton;}


	//Méthodes

	/**
	 * Efface les données rentrées dans les champs de texte du prénom, du nom, de l'email et du matricule.
	 */
	public void clearRegistrationForm(){
		this.prenomTextField.setText("");
		this.nomTextField.setText("");
		this.emailTextField.setText("");
		this.matriculeTextField.setText("");
	}

	/**
	 * Réinitialise le comboBox qui permet de choisir la session.
	 */
	public void clearSessionComboBox(){
		this.sessionComboBox.setValue("Session");
	}

	/**
	 * Réinitialise la table qui contient la liste de cours.
	 */
	public void clearTable(){
		this.loadedCourses.clear();
	}

	/**
	 * Ajoute un cours la liste observable de la table qui contient la liste de cours.
	 * @param course: prend en paramètre un cours.
	 */
	public void addLoadedCourse(Course course){
		this.loadedCourses.add(course);
	}

}
