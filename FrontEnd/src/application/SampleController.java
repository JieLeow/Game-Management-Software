package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
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

	// user name on homepage
	@FXML
	private Label unameLabel;
	
	// password on login screen
	@FXML
	private PasswordField pass;

	// login button on login screen
	@FXML
	private Button loginKey;

	// button to add shortcuts into list on homescreen
	@FXML
	private Button add;

	//button to delete selected shortcuts in the list on homescreen
	@FXML
	private Button delete;

	//button to move a shortcut up in the list
	@FXML
	private Button up;

	//button to move a shortcut down in the list
	@FXML
	private Button down;

	// hyperlink to registration page
	@FXML
	private Hyperlink register;

	//table where the shortcuts would be added to
	@FXML
	private TableView<Program> table1 = new TableView<Program>();

	//	//list of programs added by user;
	//	ObservableList<Program> programList;

	//stores current user logged in
	public static String currentUser;
	

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

	/* creates an alert
	@Param title - name of the alert box
	@Param content - what you want said in the alert box
	 */
	public void createAlert(String title, String content) {

		Alert a = new Alert(AlertType.INFORMATION);
		a.setTitle(title);
		a.setContentText(content);
		a.setHeaderText(null);
		a.initStyle(StageStyle.UTILITY);
		// show the alert
		a.showAndWait();	
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

				//disallow space or comma for username
				if(regname.getText().contains(",") || regname.getText().contains(" ")) {
					createAlert("Registration Error", "Username or Password contains invalid characters(comma or space), please enter a valid username");
				}
				else if(regname.getText() == null || regpass.getText() == null || regname.getText().trim().isEmpty() || regpass.getText().trim().isEmpty()) {
					createAlert("Registration Error", "Username or Password contains invalid characters(comma or space), please enter a valid username");
				}
				else {
					Credentials.addCredentials(regname.getText(), regpass.getText());

					//create user profile csv if not exists
					File userFile = new File(regname.getText().concat(".csv"));
					if(!userFile.isFile()) {
						DataManagement.createGamesPathFile(regname.getText());
					}

					createAlert("Registration Successful", "You are now part of us! Enter your Username and Password at the Login Page");
					handleCloseButtonAction(kev);
					Main loginPage = new Main();
					loginPage.start(stage);
				}	
			}catch(DuplicateUsernameException e) {
				createAlert("Registration Error", "Username Already Existed. Please try a different username");

			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	//Handles login procedures if register button is pressed
	@FXML
	public void buttonRegister(Event event) {
		Stage stage = new Stage();
		try {

			File f = new File("credentials.csv");
			if(!f.isFile()) {
				Credentials.createCredentialsFile();
			}

			if(regname.getText().contains(",") || regname.getText().contains(" ")) {
				createAlert("Registration Error", "Username contains invalid characters(comma or space), please enter a valid username");
			}
			else if(regname.getText() == null || regpass.getText() == null || regname.getText().trim().isEmpty() || regpass.getText().trim().isEmpty()) {
				createAlert("Registration Error", "Username or Password contains invalid characters(comma or space or nothing), please enter a valid username");
			}
			else {
				Credentials.addCredentials(regname.getText(), regpass.getText());

				//create user profile csv if not exists
				File userFile = new File(regname.getText().concat(".csv"));
				if(!userFile.isFile()) {
					DataManagement.createGamesPathFile(regname.getText());
				}

				createAlert("Registration Successful", "You are now part of us! Enter your Username and Password at the Login Page");
				handleCloseButtonAction(event);
				Main loginPage = new Main();
				loginPage.start(stage);
			}

		}catch(DuplicateUsernameException e) {
			createAlert("Registration Error", "Username Already Exists. Please try a different username");

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
					currentUser = uname.getText();
					handleCloseButtonAction(kev);
					//HomePageController mainController = new HomePageController();
					//mainController.setUserName(currentUser);
					GMS_HomePage mainPage = new GMS_HomePage();
					//					GMS_HomePage mainPage = new GMS_HomePage(uname.getText());
					//getUserShortcuts(currentUser.concat(".csv"));
					mainPage.start(stage);
					HomePageController.threadActive = true;
					HomePageController.gameStatusThread = new Thread(new GameStatusThread());
					HomePageController.gameStatusThread.start();
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
				currentUser = uname.getText();
				handleCloseButtonAction(event);
				//HomePageController mainController = new HomePageController();
				//mainController.setUserName(currentUser);
				GMS_HomePage mainPage = new GMS_HomePage();
				mainPage.start(stage);
				//retrieve user shortcuts to mainpage
				//getUserShortcuts(currentUser.concat(".csv"));
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
		currentUser = null; //resets the currentUser
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public String getUserName() {
		return uname.getText();
	}
}
