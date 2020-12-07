package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterPage extends Application {
	
	private double xOffset = 0;
	private double yOffset = 0;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Register.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("controller2.png")));
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			
			root.setOnMousePressed(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});

			root.setOnMouseDragged(new EventHandler<MouseEvent>(){
				public void handle(MouseEvent event) {
					primaryStage.setX(event.getScreenX() - xOffset);
					primaryStage.setY(event.getScreenY() - yOffset);
				}
			});

			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
