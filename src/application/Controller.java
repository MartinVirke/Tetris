package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Controller implements Initializable {

	@FXML
	private Pane rootPane;
	@FXML
	private Canvas bgCanvas;
	@FXML
	private Canvas nextBlockCanvas;
	@FXML
	private Canvas bufferCanvas1;
	@FXML
	private Canvas bufferCanvas2;
	@FXML
	private Label scoreLabel;
	@FXML
	private VBox vBox1, pauseMenu;
	@FXML
	private HBox hBox1;
	@FXML
	private Button resumeBtn, loadBtn, saveBtn, highscoresBtn, exitBtn;

	private GameLogic logic;

	private GraphicsContext blockGc;

	private boolean running;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		logic = new GameLogic(scoreLabel, pauseMenu, bgCanvas.getGraphicsContext2D(),
				nextBlockCanvas.getGraphicsContext2D());

		Platform.runLater(() -> {
			rootPane.requestFocus();
		});

		rootPane.focusedProperty().addListener((observable, newvalue, oldvalue) -> {
			Platform.runLater(() -> {
				rootPane.requestFocus();
			});
		});

		scoreLabel.setText(logic.getScore().getValue());

		rootPane.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.09, 1), null, null)));

		resumeBtn.setOnAction(event -> {
			logic.togglePause();
			logic.toggleShowMenu();
		});

		loadBtn.setOnAction(event -> {

		});

		saveBtn.setOnAction(event -> {

		});

		highscoresBtn.setOnAction(event -> {

		});

		exitBtn.setOnAction(event -> {
			running = false;
			Platform.exit();
		});

		logic.getScore().addListener((observable, newvalue, oldvalue) -> {
			Platform.runLater(() -> {
				scoreLabel.setText(observable.getValue());
			});
		});

		blockGc = bufferCanvas1.getGraphicsContext2D();
		bufferCanvas2.toFront();
		bufferCanvas1.toFront();

		rootPane.setOnKeyPressed(event -> {
			logic.keyPressed(event.getCode());
		});

		rootPane.setOnKeyReleased(event -> {
			logic.keyReleased(event.getCode());
		});

	}

	public void simpleLoop() {
		running = true;
		int updateTime = 20;
		int gameCount = 0;
		while (running) {

			if (gameCount >= updateTime) {
				logic.gameUpdate();
				gameCount = 0;
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
