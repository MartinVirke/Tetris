package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Controller implements Initializable {

	@FXML
	BorderPane borderPane;
	@FXML
	Canvas gameCanvas;
	@FXML
	Canvas blockCanvas;
	@FXML
	Canvas nextBlockCanvas;
	@FXML
	Label scoreLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		GameLogic logic = new GameLogic(scoreLabel, gameCanvas.getGraphicsContext2D(),
				nextBlockCanvas.getGraphicsContext2D(), blockCanvas.getGraphicsContext2D());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				borderPane.requestFocus();
			}
		});

		// nextBlockCanvas.widthProperty().bind(borderPane.widthProperty().divide(2.0f).subtract(1));
		// nextBlockCanvas.heightProperty().bind(borderPane.heightProperty().divide(2.0f).subtract(1));
		// blockCanvas.widthProperty().bind(borderPane.widthProperty().divide(6.0f));
		// gameCanvas.widthProperty().bind(borderPane.widthProperty().divide(6.0f));

		Thread thread = new Thread(() -> {
			mainLoop(logic);
		});
		thread.start();

		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// while (logic.isRunning()){
		// try {
		// Thread.sleep(logic.getUpdateTime());
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// logic.updateLogic();
		// }
		// }
		// });

		borderPane.setOnKeyPressed(event -> {
			logic.keyPressed(event.getCode());
		});

		borderPane.setOnKeyReleased(event -> {
			logic.keyReleased(event.getCode());
		});

	}
	
	private static final int UPDATE_RATE = 60;
	private static final float UPDATE_PERIOD = 1000F / UPDATE_RATE;

	private void mainLoop(GameLogic logic) {
		boolean running = true;

		long beginTime, timeTaken;
		long timeLeft;
			while (running) {
			beginTime = System.nanoTime();

			logic.drawGraphics();

			timeTaken = System.nanoTime() - beginTime;
			timeLeft = (long) (timeTaken / 16000F);
			if (timeLeft < UPDATE_PERIOD) {
				try {
					Thread.sleep(timeLeft);
				} catch (InterruptedException ex) {
				}
			}
		}
	}

}
