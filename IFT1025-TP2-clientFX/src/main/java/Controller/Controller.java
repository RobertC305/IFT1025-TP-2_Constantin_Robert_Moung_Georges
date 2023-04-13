package Controller;

import server.models.Client;
import server.models.Course;
import server.models.RegistrationForm;
import View.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

import java.io.IOException;

/*
 * Cette classe lie le modele avec la vue. 
 * À noter qu'elle ne connaît ni de detailles d'implementation 
 * du comportement, ni de detailles de structuration du GUI.
 * 
 */
public class Controller {

	private Client client;
	private View view;
	private Course selectedCourse = null;

	public Controller(Client client, View view) {

		this.client = client;
		this.view = view;

		 /*
		 * La definition du comportement de chaque handler 
		 * est mise dans sa propre méthode auxiliaire. Il pourrait être même 
		 * dans sa propre classe entière: ne niveau de decouplage
		 * depend de la complexité de l'application
		 */

		//Affiche les cours de la session choisi dans la table
		this.view.getSessionConfirmationButton().setOnAction((action) -> {
			this.chargerSession();
		});

		//Retourne l'objet sélectionner dans la table de liste de cours
		this.view.getCourseTable().getSelectionModel().selectedIndexProperty().addListener((num) -> {
			this.selectedCourse = this.coursChoisi();
		});

		//Envoie les données d'inscription sous forme d'objet RegistrationForm au serveur
		this.view.getRegistrationConfirmationButton().setOnAction((action) -> {
			this.inscription();
		});

	}

	private void inscription(){
		String prenom = this.view.getPrenomTextField().getText().toString();
		String nom = this.view.getNomTextField().getText().toString();
		String matricule = this.view.getMatriculeTextField().getText().toString();
		String email = this.view.getEmailTextField().getText().toString();
		Course course = selectedCourse;

		RegistrationForm registrationForm = new RegistrationForm(prenom,nom,email,matricule,course);
		try{
		Client.inscrire(registrationForm);
		this.clearApplicationForm();
		}
		catch (IOException e){throw new RuntimeException(e);}
		catch (ClassNotFoundException e){throw new RuntimeException(e);}

	}
	private void chargerSession(){
		this.view.clearTable();
		try{
			Client.charger(this.view.getSessionComboBox().getValue().toString());

			//Ajout des cours dans la liste de cours observables
			for(Course course : this.client.getListCoursesConsulted()){
			this.view.addLoadedCourse(course);
			}

		}
		catch (IOException e){throw new RuntimeException(e);}
		catch (ClassNotFoundException e){throw new RuntimeException(e);}
	}

	private Course coursChoisi() {

		TableView table = this.view.getCourseTable();
		Course course = null;

		if (table.getItems().isEmpty() == false) {
			int index = table.getSelectionModel().selectedIndexProperty().get();
			course = (Course) table.getItems().get(index);
		}

		return course;
	}

	private void clearApplicationForm(){
		this.view.clearRegistrationForm();
		this.view.clearSessionComboBox();
		this.view.clearTable();
	}

}