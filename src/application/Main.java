package application;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * View class which handles the creation of JavaFX and display of graphical
 * elements.
 * 
 * @author Martin Virke
 */

public class Main extends Application {

	/**
	 * Sets up the JavaFX part of the application, and creates the controller
	 * class instance through a FXML loader.
	 */

	@Override
	public void start(Stage primaryStage) {
		try {
			URL location = getClass().getResource("View.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(location);
			Parent root;
			root = fxmlLoader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
		}
	}

	/**
	 * Automatically generated code.
	 * 
	 * @param args
	 *            Launch arguments.
	 */

	public static void main(String[] args) {
		launch(args);
	}
}
