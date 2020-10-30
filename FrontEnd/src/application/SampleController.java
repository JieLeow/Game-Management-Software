package application;

import java.net.URL;
import java.util.ResourceBundle;

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

	@FXML
	private Button closeButton;

	@FXML
    private Button minimizeButton;
	
	@FXML
	private TextField uname;

	@FXML
	private PasswordField pass;

	@FXML
	private Button loginKey;

	@FXML
	private Button add;

	//Close current window
	@FXML
	public void handleCloseButtonAction(Event event) {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}

	@FXML
    void minimize(ActionEvent event) {
		Stage stage = (Stage) minimizeButton.getScene().getWindow();
		stage.setIconified(true);
	}
	
	@FXML
	void hovers() {
		add.setStyle("button-hover-color: #019101;");
	}
	
	//if you press enter on any of the text fields, login process will initiate
	@FXML
	public void textLogin(KeyEvent kev) {
		Stage stage = new Stage();
		if(kev.getCode() == KeyCode.ENTER) {
			if (uname.getText().equals("admin") && pass.getText().equals("password")) {
				handleCloseButtonAction(kev);
				GMS_HomePage mainPage = new GMS_HomePage();
				mainPage.start(stage);
			}
			else {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Error");
				a.setContentText("Incorrect Username or Password");
				a.setHeaderText(null);
				//a.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("controller.png"))));
				a.initStyle(StageStyle.UTILITY);
				// show the alert
				a.show();
			}
		}
	}

	//Handles login procedures
	@FXML
	public void buttonLogin(Event event) {
		Stage stage = new Stage();
		if (uname.getText().equals("admin") && pass.getText().equals("password")) {
			try {
				handleCloseButtonAction(event);
				BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("GMSHomepage.fxml"));
				Scene scene = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
				stage.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setTitle("Error");
			a.setContentText("Incorrect Username or Password");
			a.setHeaderText(null);
			//a.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("controller.png"))));
			a.initStyle(StageStyle.UTILITY);
			// show the alert
			a.show();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}
}
