package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Controller implements Initializable {

	@FXML
	BorderPane borderPane;
	@FXML
	Canvas gameCanvas;
	@FXML
	Canvas nextBlockCanvas;
	@FXML
	Canvas bufferCanvas1;
	@FXML
	Canvas bufferCanvas2;
	@FXML
	Label scoreLabel;

	GameLogic logic;

	GraphicsContext blockGc;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		logic = new GameLogic(scoreLabel, gameCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				borderPane.requestFocus();
			}
		});

//		scoreLabel.textProperty().bind(logic.getScore());
		
		scoreLabel.setText(logic.getScore().getValue());
		
		logic.getScore().addListener((observable, newvalue, oldvalue) -> {
//			Platform.runLater(new Runnable() {
//				@Override
//				public void run() {
//					scoreLabel.setText(observable.getValue());
//				}
//			});
			Platform.runLater(()->{
					scoreLabel.setText(observable.getValue());
			});
			
		});

		// blockCanvas.setVisible(false);
		// blockCanvas = bufferCanvas1;
		blockGc = bufferCanvas1.getGraphicsContext2D();
		bufferCanvas2.toFront();
		bufferCanvas1.toFront();

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
		boolean toggle = false;
		int updateTime = 20;
		int gameCount = 0;
		while (running) {

			if (gameCount >= updateTime) {
				logic.gameUpdate();
				gameCount = 0;
			}

			if (toggle) {
				bufferCanvas1.setVisible(true);
				bufferCanvas2.setVisible(false);
				blockGc = bufferCanvas2.getGraphicsContext2D();
				toggle = false;
			} else {
				bufferCanvas2.setVisible(true);
				bufferCanvas1.setVisible(false);
				blockGc = bufferCanvas1.getGraphicsContext2D();
				toggle = true;
			}

			logic.drawGraphics(blockGc);

			gameCount++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
