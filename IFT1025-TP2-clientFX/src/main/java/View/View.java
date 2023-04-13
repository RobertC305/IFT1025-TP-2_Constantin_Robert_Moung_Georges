package View;

import Model.Course;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/*
 * Dans cette classe nous definissons les éléments graphiques de notre
 * application.
 */
public class View extends HBox {

	//Diviser le HBox en 2 VBox de largeurs égales
	private VBox courseVBox = new VBox(250);
	private VBox registrationVBox = new VBox(250);

	private Text courseTitle = new Text("Liste des cours");
	private Text registrationTitle = new Text("Formulaire d'inscription");
	private Font titlesFont = Font.font("Verdana", FontWeight.BOLD, 15);

	private VBox registrationFieldsVBox = new VBox();

	private HBox prenomHBOx = new HBox();
	private Label prenomLabel = new Label("Prénom");
	private TextField prenomTextField = new TextField();

	private HBox nomHBOx = new HBox();
	private Label nomLabel = new Label("Nom");
	private TextField nomTextField = new TextField();

	private HBox emailHBOx = new HBox();
	private Label emailLabel = new Label("Email");
	private TextField emailTextField = new TextField();

	private HBox matriculeHBOx = new HBox();
	private Label matriculeLabel = new Label("Matricule");
	private TextField matriculeTextField = new TextField();

	private Button registrationConfirmationButton = new Button("envoyer");
	private ComboBox sessionComboBox = new ComboBox();
	private  Button sessionConfirmationButton = new Button("charger");
	private TableView courseTable = new TableView();
	private TableColumn courseCodeTableCol = new TableColumn("Code");
	private TableColumn courseNameTableCol = new TableColumn("Cours");

	private HBox courseSessionHBox = new HBox();

	public View() {

		/*
		this.registrationGridPane.setAlignment(Pos.CENTER);
		this.registrationGridPane.setHgap(15);
		this.registrationGridPane.setVgap(20);
		this.registrationGridPane.setPadding(new Insets(25,25,25,25));

		this.registrationGridPane.add(prenomLabel,0,0);
		this.registrationGridPane.add(prenomTextField,0,1);

		this.registrationGridPane.add(nomLabel,1,0);
		this.registrationGridPane.add(nomTextField,1,1);

		this.registrationGridPane.add(emailLabel,2,0);
		this.registrationGridPane.add(emailTextField,2,1);

		this.registrationGridPane.add(matriculeLabel,3,0);
		this.registrationGridPane.add(matriculeTextField,3,1);
		 */
		this.registrationTitle.setFont(titlesFont);
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

		this.registrationFieldsVBox.getChildren().addAll(prenomHBOx,nomHBOx,emailHBOx,matriculeHBOx);
		this.registrationFieldsVBox.setAlignment(Pos.CENTER);
		this.registrationFieldsVBox.setSpacing(10);

		this.registrationVBox.getChildren().add(registrationTitle);
		this.registrationVBox.getChildren().add(this.registrationFieldsVBox);
		this.registrationVBox.getChildren().add(registrationConfirmationButton);
		this.registrationVBox.setAlignment(Pos.TOP_CENTER);
		this.registrationVBox.setSpacing(20);

		//Course VBox
		this.courseTitle.setFont(titlesFont);

		this.courseCodeTableCol.setCellValueFactory(new PropertyValueFactory<Course,String>("code"));
		this.courseNameTableCol.setCellValueFactory(new PropertyValueFactory<Course,String>("name"));
		this.courseTable.getColumns().add(courseCodeTableCol);
		this.courseTable.getColumns().add(courseNameTableCol);
		this.courseTable.setEditable(false);

		this.sessionComboBox.getItems().addAll("Automne","Hiver","Ete");
		this.sessionComboBox.setValue("Session");

		this.courseSessionHBox.getChildren().add(sessionComboBox);
		this.courseSessionHBox.getChildren().add(sessionConfirmationButton);
		this.courseSessionHBox.setAlignment(Pos.CENTER);
		this.courseSessionHBox.setSpacing(10);
		this.courseSessionHBox.setPadding(new Insets(0,0,10,0));

		this.courseVBox.getChildren().add(courseTitle);
		this.courseVBox.getChildren().add(courseTable);
		this.courseVBox.getChildren().add(new Separator());
		this.courseVBox.getChildren().add(courseSessionHBox);
		this.courseVBox.setAlignment(Pos.TOP_CENTER);
		this.courseVBox.setSpacing(20);
		//this.courseVBox.setPadding(new Insets(0,30,0,0));


		//View HBox
		this.getChildren().add(courseVBox);
		this.getChildren().add(new Separator(Orientation.VERTICAL));
		this.getChildren().add(registrationVBox);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10);
		this.setPadding(new Insets(5,0,0,0));

	}

	//Course VBox
	public TableView getCourseTable(){return this.courseTable;}
	public TableColumn getCourseCodeTableCol() {return courseCodeTableCol;}
	public TableColumn getCourseNameTableCol() {return courseNameTableCol;}
	public ComboBox getSessionComboBox() {return sessionComboBox;}
	public Button getSessionConfirmationButton() {return sessionConfirmationButton;}


	//Registration VBox
	public TextField getPrenomTextField() {return prenomTextField;}
	public TextField getNomTextField() {return nomTextField;}
	public TextField getEmailTextField() {return emailTextField;}
	public TextField getMatriculeTextField() {return matriculeTextField;}
	public Button getRegistrationConfirmationButton() {return registrationConfirmationButton;}


	public void clearRegistrationForm(){
		this.prenomTextField.setText("");
		this.nomTextField.setText("");
		this.emailTextField.setText("");
		this.matriculeTextField.setText("");
	}

	public void clearSessionComboBox(){
		this.sessionComboBox.setValue(null);
	}

}
