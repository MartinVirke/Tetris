package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller implements Initializable {

	@FXML
	BorderPane borderPane;
	@FXML
	Canvas canvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		GameLogic logic = new GameLogic();

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				while (!borderPane.isFocused())
					borderPane.requestFocus();
			}
		});
		
		Thread thread = new Thread(()->{
			logic.drawGraphics(canvas.getGraphicsContext2D());
		});
		thread.start();
		
//		Platform.runLater(new Runnable() {
//			@Override
//			public void run() {
//				while (logic.isRunning()){
//					try {
//						Thread.sleep(logic.getUpdateTime());
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					logic.updateLogic();
//				}
//			}
//		});
		
		borderPane.setOnKeyPressed(event -> {
			logic.keyPressed(event.getCode());
		});
		
	}

}
