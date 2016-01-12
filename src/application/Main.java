package application;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;

public class Main extends Application {
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

			Controller ctrl = fxmlLoader.getController();
			Thread graphicsThread = new Thread(()->{
				ctrl.simpleLoop();
			});
			graphicsThread.setName("graphicsThread");
			graphicsThread.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
