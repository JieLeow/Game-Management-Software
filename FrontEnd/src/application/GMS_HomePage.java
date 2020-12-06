package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GMS_HomePage extends Application {

	private double xOffset = 0;
	private double yOffset = 0;
	private String uname;

	
	@Override
	public void start(Stage stage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("GMSHomepage.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.getIcons().add(new Image(getClass().getResourceAsStream("controller2.png")));

			root.setOnMousePressed(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});

			root.setOnMouseDragged(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent event) {
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			});
			stage.show();
			//getUserShortcuts(uname.concat(".csv"));

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setUname(String username) {
		uname = username;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
