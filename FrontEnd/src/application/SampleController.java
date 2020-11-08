package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.EventHandler;

public class SampleController implements Initializable{

	// assign this to all buttons that close the application
	@FXML
	private Button closeButton;

	// assign this to all buttons that minimize the application
	@FXML
    private Button minimizeButton;
	
	//user name on registration screen
	@FXML
	private TextField regname;
	
	
	@FXML
	//user password on registration screen
	private PasswordField regpass;
	
	// user name on login screen
	@FXML
	private TextField uname;

	// password on login screen
	@FXML
	private PasswordField pass;

	// login button on login screen
	@FXML
	private Button loginKey;

	// button to add shortcuts into list on homescreen
	@FXML
	private Button add;
	
	// hyperlink to registration page
	@FXML
	private Hyperlink register;
	

	//Close current window
	@FXML
	public void handleCloseButtonAction(Event event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
	
	//Minimizes the window
	@FXML
    void minimize(ActionEvent event) {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.setIconified(true);
	}
	
	// When the hyperlink to register page is activated, send user to the registration screen
	@FXML
	void goToRegisterPage(ActionEvent event) {
		handleCloseButtonAction(event);
		Stage stageReg = new Stage();
		RegisterPage registers = new RegisterPage();
		registers.start(stageReg);
	}
	
	// doesn't work
	@FXML
	void hovers() {
		add.setStyle("button-hover-color: #019101;");
	}
	
	
	public void createAlert(String title, String content) {
		
		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle(title);
		a.setContentText(content);
		a.setHeaderText(null);
		a.initStyle(StageStyle.UTILITY);
		// show the alert
		a.show();
		
	}
	
	
	//if you press enter on any of the text fields, registration process will initiate
	@FXML
	public void textRegister(KeyEvent kev) {
		Stage stage = new Stage();
		if(kev.getCode() == KeyCode.ENTER) {
			
			try {
				
				File f = new File("credentials.csv");
				if(!f.isFile()) {
					Credentials.createCredentialsFile();
				}
				
				Credentials.addCredentials(regname.getText(), regpass.getText());
				createAlert("Registration Successful", "You are now part of us! Click return to proceed to the Log in Page");
				
				
				
				
			}catch(DuplicateUsernameException e) {
				createAlert("Registration Error", "Username Already Existed. Please try a different username");
			
			
			}catch(InvalidCharacterException e) {
				
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	//Handles login procedures if register button is pressed
	public void buttonRegister(Event event) {
		Stage stage = new Stage();
		
		try {
			Credentials.createCredentialsFile();
			
			if(regname.getText().contains(",")) {
				createAlert("Registration Error", "Username contains a comma, please enter a valid username");

			}
			
			else {
				Credentials.addCredentials(regname.getText(), regpass.getText());
				createAlert("Registration Successful", "You are now part of us! Click return to proceed to the Log in Page");
			}
			
		}catch(DuplicateUsernameException e) {
			createAlert("Registration Error", "Username Already Existed. Please try a different username");
		
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//if you press enter on any of the text fields, login process will initiate
	@FXML
	public void textLogin(KeyEvent kev) {
		Stage stage = new Stage();
		if(kev.getCode() == KeyCode.ENTER) {
			try {
				
				boolean exist = Credentials.validateCredentials(uname.getText(), pass.getText());

				if(exist) {
					handleCloseButtonAction(kev);
					GMS_HomePage mainPage = new GMS_HomePage();
					mainPage.start(stage);
				}
				else {
					createAlert("Incorrect Password", "Your password is incorrect, Please ensure there are no typos");
				}
				
				
			}catch(UsernameNotFoundException e) {
				createAlert("Login Error", "There's no record of your username in our System. Please Try Again.");
			
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	//Handles login procedures if login button is pressed
	@FXML
	public void buttonLogin(Event event) {
		Stage stage = new Stage();
		try {
			
			boolean exist = Credentials.validateCredentials(uname.getText(), pass.getText());

			if(exist) {
				handleCloseButtonAction(event);
				GMS_HomePage mainPage = new GMS_HomePage();
				mainPage.start(stage);
			}
			else {
				createAlert("Incorrect Password", "Your password is incorrect, Please ensure there are no typos");
			}
			
			
		}catch(UsernameNotFoundException e) {
			createAlert("Login Error", "There's no record of your username in our System. Please Try Again.");
		
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	// Used when the user needs to get back to the login screen
	public void backToLogin(Event event) {
		handleCloseButtonAction(event);
		Stage stage = new Stage();
		Main loginPage = new Main();
		loginPage.start(stage);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
}
