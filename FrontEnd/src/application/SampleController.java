package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;

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
	private TableView<Program> table1;
	
	//stores current user logged in
	private static String currentUser;
	

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
					GMS_HomePage mainPage = new GMS_HomePage();
//					GMS_HomePage mainPage = new GMS_HomePage(uname.getText());
					mainPage.start(stage);
					
					//retrieve user shortcuts to mainpage
					getUserShortcuts(currentUser.concat(".csv"));
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
				GMS_HomePage mainPage = new GMS_HomePage();
				mainPage.start(stage);
				//retrieve user shortcuts to mainpage
//				getUserShortcuts(currentUser.concat(".csv"));
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

//	public void addShortcut() {
//		Stage stage = new Stage();
//		try {
//			File f = new File("tableData.csv");
//			if(!f.isFile()) {
//				GMSTable.createTableFile();
//			}
//			FileChooser fileChooser = new FileChooser();
//			fileChooser.setTitle("Select Game Shortcut");
//			File shortcut = fileChooser.showOpenDialog(stage);
//			String fileName;
//			if (shortcut != null) {
//				String fileName = shortcut.getAbsolutePath();
//			}
//			GMSTable.addShortcut(fileName);
//		}
//		catch(DuplicateShortcutException e) {
//			createAlert("Registration Error", "Username Already Exists. Please try a different username");
//
//		}catch(IOException e) {
//			e.printStackTrace();
//	}
//	}
	
	//add a program shortcut to the list in homescreen using the add button 
	public void chooseFile(ActionEvent event) {
	
		System.out.println(currentUser);
		
		String filePath, fileName;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		File selectedFile = fileChooser.showOpenDialog(null);
		if(selectedFile != null) {
			filePath = selectedFile.getAbsolutePath();
			fileName = selectedFile.getName();
		}else {
			filePath = null;
			fileName = null;
			}
		
		try {
			System.out.println(currentUser);
			DataManagement.addGame(currentUser, filePath);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		getUserShortcuts(currentUser.concat(".csv"));
		


		
		
		//TODO: also, need to load the files to the mainPage upon login. NOT IN THIS METHOD THO
		
	}
	

	
	//remove selected program shortcut in the list in homescreen using the delete button
	public void removeFile(ActionEvent event) {
		
		Program selectedProgram = table1.getSelectionModel().getSelectedItem();
		
		if(selectedProgram != null) {
			table1.getItems().remove(selectedProgram);
		}
		
		//TODO: handle runtime nullpointer exception
	
		String selectedPath = selectedProgram.getProgramDirectory();
		//check from user csv file, loop through the programs by name and remove.
		//calls delete game function.
		
	}
	
	public void getUserShortcuts(String userCsv) {
		//called when user logs in, add files, or remove files;
	
		ObservableList<Program> data = table1.getItems(); //TODO: why null?
		System.out.println(table1);
//		data.clear();
		System.out.println(table1.getItems());
		String gamePathRow; 
		//loop through user's csv file and retrieve game data to tableView
		try {
			BufferedReader gameFileReader = new BufferedReader(new FileReader(userCsv));
			gamePathRow = gameFileReader.readLine(); //reads first line
			while((gamePathRow = gameFileReader.readLine()) != null) {
				
				data.add(new Program("test", gamePathRow, "0", "0")); //adds program to table view
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	public void moveProgramUp(ActionEvent event) {
		
		
	}
	
	
	public void moveProgramDown(ActionEvent event) {
	
		
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
}
