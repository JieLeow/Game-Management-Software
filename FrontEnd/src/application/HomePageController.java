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

public class HomePageController implements Initializable{

	//table where the shortcuts would be added to
	@FXML
	private TableView<Program> table1 = new TableView<Program>();

	@FXML
	private Label label1;
	
	@FXML 
	private Button minimizeButton;

	@FXML
	private Button closeButton;

	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String user) {
		userName = user;
		initialize(null, null);
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

	//Minimizes the window
	@FXML
	void minimize(ActionEvent event) {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.setIconified(true);
	}

	//Close current window
	@FXML
	public void handleCloseButtonAction(Event event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}	

	// Used when the user needs to get back to the login screen
	public void backToLogin(Event event) {
		handleCloseButtonAction(event);
		Stage stage = new Stage();
		Main loginPage = new Main();
		loginPage.start(stage);
		userName = null; //resets the currentUser
	}

	//add a program shortcut to the list in homescreen using the add button 
	public void chooseFile(ActionEvent event) {

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
			System.out.println(SampleController.currentUser);
			if(filePath != null) {
				
				//update to ethan's method
				if(ProgramFile.validFileExtension(filePath)) {
					DataManagement.addGame(SampleController.currentUser, filePath,fileName);
				}
				else {
					createAlert("Invalid File Extension", "Please add only games with jar and exe extensions");
					System.out.println("File Path that you want to add is:" + filePath );

				}
			
			}
		}
		catch(DuplicatePathException e) {
			System.out.println("Game was already added");
			createAlert("Duplicate Game Added", "That game is already in your list! You need to try something new.");

		}
		catch(IOException e) {
			e.printStackTrace();
		}

		//re-populate the tableView with latest games in user.csv
		getUserShortcuts(userName.concat(".csv"));

		//TODO: also, need to load the files to the mainPage upon login. NOT IN THIS METHOD THO

	}

	//remove selected program shortcut in the list in homescreen using the delete button
	public void removeFile(ActionEvent event) {

		Program selectedProgram = table1.getSelectionModel().getSelectedItem();
		String selectedPath;

		if(selectedProgram == null) {
			createAlert("No File Selected", "Your list is either empty or you have not selected a game yet.");
			return;
		}else {
			selectedPath = selectedProgram.getProgramDirectory().trim();
			table1.getItems().remove(selectedProgram);
		}

		try {
			DataManagement.deleteGame(userName, selectedPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//then fetch data again from user's csv
		getUserShortcuts(userName.concat(".csv"));
	}


	public void getUserShortcuts(String userCsv) {
		//called when user logs in, add files, or remove files;

		//table1 = new TableView<Program>();
		ObservableList<Program> data = FXCollections.observableArrayList();

		//		System.out.println("What is table 1: " + table1);  //TODO: IT'S NULL??

		data.clear();
		//		System.out.println(table1.getItems());
		String gamePathRow; 

		//loop through user's csv file and retrieve game data to tableView
		try {
			BufferedReader gameFileReader = new BufferedReader(new FileReader(userCsv));
			gameFileReader.readLine(); //read to skip first (header) line
			while((gamePathRow = gameFileReader.readLine()) != null) {

				String[] gameInfo = gamePathRow.split(",");
				String gameName = gameInfo[0];
				String gamePath = gameInfo[1];


				data.add(new Program(gameName, gamePath, "Inactive")); //adds program to table view
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		table1.setItems(data);
	}

	//method to allow user to execute a selected game in the tableView
	public void runGame() {
		ProgramFile file;
		
		Program selectedProgram = table1.getSelectionModel().getSelectedItem();
		String selectedPath;
		if(selectedProgram == null) {
			createAlert("No File Selected", "Your list is either empty or you have not selected a game yet.");
			return;
		}else {
			selectedPath = selectedProgram.getProgramDirectory().trim();
			System.out.println("Selected path to be executed is:" + selectedPath);
			try {
				file = new ProgramFile(selectedPath);
				file.execute();	
			}
			catch(UnsupportedFileExtension e) {
				e.printStackTrace();
			}
			
		}
		
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userName = SampleController.currentUser;
		getUserShortcuts(userName.concat(".csv"));
		label1.setText(userName);
	}

}
