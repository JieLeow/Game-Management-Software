package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

	@FXML
	private Circle circle1;

	public static Timer gameStatusTimer;

	//	private ObservableList<Program> data;
	public static ObservableList<Program> data;

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
		gameStatusTimer.cancel();

		stage.close();
	}	

	// Used when the user needs to get back to the login screen
	public void backToLogin(Event event) {
		handleCloseButtonAction(event);
		Stage stage = new Stage();
		Main loginPage = new Main();
		loginPage.start(stage);
		InitializationController.currentUser = null; //resets the currentUser
	}

	//add a program shortcut to the list in homescreen using the add button 
	@FXML
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
			System.out.println(InitializationController.currentUser);
			if(filePath != null) {

				//update to ethan's method
				if(ProgramFile.validFileExtension(filePath)) {
					DataManagement.addGame(InitializationController.currentUser, filePath,fileName);
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
		getUserShortcuts(InitializationController.currentUser.concat(".csv"));

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
			DataManagement.deleteGame(InitializationController.currentUser, selectedPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//then fetch data again from user's csv
		getUserShortcuts(InitializationController.currentUser.concat(".csv"));
	}


	public void getUserShortcuts(String userCsv) {
		//called when user logs in, add files, or remove files;
		data = FXCollections.observableArrayList();
		String gamePathRow; 

		//loop through user's csv file and retrieve game data to tableView
		try {
			BufferedReader gameFileReader = new BufferedReader(new FileReader(userCsv));
			gameFileReader.readLine(); //read to skip first (header) line
			while((gamePathRow = gameFileReader.readLine()) != null) {

				String[] gameInfo = gamePathRow.split(",");
				String gameName = gameInfo[0];
				String gamePath = gameInfo[1];

				//TODO: fix this, no magic numbers
				data.add(new Program(gameName, gamePath, "Inactive")); //adds program to table view
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		table1.setItems(data);
	}


	@FXML
	public void terminateGame() {
		ProgramFile file;

		Program selectedProgram = table1.getSelectionModel().getSelectedItem();
		String selectedPath;
		if(selectedProgram == null) {
			createAlert("No File Selected", "Your list is either empty or you have not selected a game yet.");
			return;
		}else {
			selectedPath = selectedProgram.getProgramDirectory().trim();
			try {
				file = new ProgramFile(selectedPath);
				file.terminate();	
			}
			catch(UnsupportedFileExtension e) {
				e.printStackTrace();
			}
		}
	}

	//method to allow user to execute a selected game in the tableView
	@FXML
	public void runGame() {
		ProgramFile file;

		Program selectedProgram = table1.getSelectionModel().getSelectedItem();
		String selectedPath;
		if(selectedProgram == null) {
			createAlert("No File Selected", "Your list is either empty or you have not selected a game yet.");
			return;
		}else {
			selectedPath = selectedProgram.getProgramDirectory().trim();
			try {
				file = new ProgramFile(selectedPath);
				file.execute();	
			}
			catch(UnsupportedFileExtension e) {
				e.printStackTrace();
			}
		}
	}


	void startTimer() {
		gameStatusTimer = new Timer();
		gameStatusTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(checkGameStatus()){
					//change icon to green
					circle1.setFill(Color.GREEN);
				}
				else {
					//change icon to red
					circle1.setFill(Color.RED);

				}
			} 
		}, 0, 1000);
	}

	public boolean checkGameStatus(){

		if (HomePageController.data != null){   	    
			for(Program file: HomePageController.data) {
				file.getProgramName();
				String program = file.getProgramName();   //or any other process
				String listOfProcesses = getCommandOutput("tasklist");
				if (listOfProcesses == null || listOfProcesses.isEmpty()) {
				} else {
					if (listOfProcesses.contains(program)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getCommandOutput(String command)  {
		String output = null;       //the string to return
		Process process = null;
		BufferedReader reader = null;
		InputStreamReader streamReader = null;
		InputStream stream = null;

		try {
			process = Runtime.getRuntime().exec(command);

			//Get stream of the console running the command
			stream = process.getInputStream();
			streamReader = new InputStreamReader(stream);
			reader = new BufferedReader(streamReader);

			String currentLine = null;  //store current line of output from the cmd
			StringBuilder commandOutput = new StringBuilder();  //build up the output from cmd
			while ((currentLine = reader.readLine()) != null) {
				commandOutput.append(currentLine + "\n");
			}

			int returnCode = process.waitFor();
			if (returnCode == 0) {
				output = commandOutput.toString();
			}

		} catch (IOException e) {
			output = null;
		} catch (InterruptedException e) {
		} finally {
			//Close all inputs / readers
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
				}
			} 
			if (streamReader != null) {
				try {
					streamReader.close();
				} catch (IOException e) {
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		//Return the output from the command - may be null if an error occured
		return output;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		startTimer();
		getUserShortcuts(InitializationController.currentUser.concat(".csv"));
		label1.setText(InitializationController.currentUser);
	}
}