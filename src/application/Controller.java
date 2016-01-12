package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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

	GameLogic logic;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		logic = new GameLogic(scoreLabel, gameCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D(),
				blockCanvas.getGraphicsContext2D());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				borderPane.requestFocus();
			}
		});

		blockCanvas.toFront();

		// gameCanvas.
		// BufferStrategy bstrat;

		// nextBlockCanvas.widthProperty().bind(borderPane.widthProperty().divide(2.0f).subtract(1));
		// nextBlockCanvas.heightProperty().bind(borderPane.heightProperty().divide(2.0f).subtract(1));
		// blockCanvas.widthProperty().bind(borderPane.widthProperty().divide(6.0f));
		// gameCanvas.widthProperty().bind(borderPane.widthProperty().divide(6.0f));

		// Thread thread = new Thread(() -> {
		// mainLoop();
		//
		// });
		// thread.start();
		//
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

	// private static final int UPDATE_RATE = 60;
	// private static final float UPDATE_PERIOD = 1000F / UPDATE_RATE;

	// public void mainLoop() {
	// boolean running = true;
	//
	// long timeTaken;
	// long beginTime;
	// long timeLeft;
	//
	// while (running) {
	// System.out.println("running");
	// beginTime = System.nanoTime();
	//
	// logic.drawGraphics();
	//
	// timeTaken = System.nanoTime() - beginTime;
	// timeLeft = (long) (timeTaken / 16000F);
	// if (timeLeft < UPDATE_PERIOD) {
	// try {
	//// System.out.println(timeLeft);
	// Thread.sleep(timeLeft + 25);
	// } catch (InterruptedException ex) {
	// }
	// }
	// }
	// }

	// private float

	public void simpleLoop() {
		boolean running = true;
		int updateTime = 30;
		int gameCount = 0;
		while (running ) {

			if(gameCount >= updateTime){
				logic.gameUpdate();
				gameCount = 0;
			}
			logic.drawGraphics();
			gameCount++;
			try{
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
