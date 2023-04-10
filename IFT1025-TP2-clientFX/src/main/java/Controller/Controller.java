package Controller;

import Model.Client;
import Model.Course;
import Model.RegistrationForm;
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
	final ObservableList<Course> loadedCourses = FXCollections.observableArrayList();
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
		String prenom = this.view.getPrenomTextField().toString();
		String nom = this.view.getNomTextField().toString();
		String matricule = this.view.getMatriculeTextField().toString();
		String email = this.view.getEmailTextField().toString();
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
		this.loadedCourses.clear();
		try{
			Client.charger(this.view.getSessionComboBox().getValue().toString());

			//Ajout des cours dans la liste de cours observables
			for(Course course : this.client.getListCoursesConsulted()){
			this.loadedCourses.add(course);
			}

		}
		catch (IOException e){throw new RuntimeException(e);}
		catch (ClassNotFoundException e){throw new RuntimeException(e);}
	}

	private Course coursChoisi(){
		TableView table = this.view.getCourseTable();
		Course course = null;

		int index = table.getSelectionModel().selectedIndexProperty().get();
		course = (Course) table.getItems().get(index);
		return course;
	}

	private void clearApplicationForm(){
		this.view.clearRegistrationForm();
		this.view.clearSessionComboBox();
		this.loadedCourses.clear();
	}

}