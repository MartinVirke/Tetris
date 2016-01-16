package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller implements Initializable {

	@FXML
	private Pane rootPane;
	@FXML
	private Canvas blockCanvas;
	@FXML
	private Canvas bgCanvas;
	@FXML
	private Canvas nextBlockCanvas;
	@FXML
	private Label scoreLabel;
	@FXML
	private VBox vBox1, pauseMenu;
	@FXML
	private HBox hBox1;
	@FXML
	private Button resumeBtn, loadBtn, saveBtn, highscoresBtn, exitBtn, okBtn;
	@FXML
	private BorderPane highscoreMenu;
	@FXML
	private BorderPane gameOverMenu;
	@FXML
	private Label highscoreLabel;

	private GameLogic logic;

	private boolean running;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		logic = new GameLogic(pauseMenu, bgCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());

		ReadWriteHandler rwHandler = new ReadWriteHandler();
		HighscoreHandler hsHandler = new HighscoreHandler();

		highscoreMenu.setBackground(new Background(new BackgroundFill(new Color(0.5, 0.5, 0.9, 1), null, null)));

		blockCanvas.toFront();
		blockCanvas.setEffect(new Glow(0.9));
		nextBlockCanvas.setEffect(new Glow(0.9));
		highscoreMenu.toFront();

		initScoreListener();

		Platform.runLater(() -> {
			rootPane.requestFocus();
		});

		rootPane.focusedProperty().addListener((observable, newvalue, oldvalue) -> {
			Platform.runLater(() -> {
				if (logic.getState() == State.RUNNING) {
					setGameFocus();
				}
			});
		});

		scoreLabel.setText(String.valueOf(logic.getScore().getSerializableValue()));

		rootPane.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.09, 1), null, null)));

		resumeBtn.setOnAction(event -> {
			logic.togglePause();
			logic.toggleShowMenu();
			setGameFocus();
		});
		
		loadBtn.setOnAction(event -> {
			logic = (GameLogic) rwHandler.readFiles(logic);
			logic.initFromSave(pauseMenu, bgCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());
			initScoreListener();
			logic.drawGraphics(blockCanvas.getGraphicsContext2D());
		});

		saveBtn.setOnAction(event -> {
			rwHandler.writeFile(logic);
		});

		highscoresBtn.setOnAction(event -> {
			highscoreMenu.setVisible(!highscoreMenu.isVisible());
		});

		okBtn.setOnAction(event -> {
			highscoreMenu.setVisible(!highscoreMenu.isVisible());
		});

		exitBtn.setOnAction(event -> {
			running = false;
			Platform.exit();
		});

		rootPane.setOnKeyPressed(event -> {
			logic.keyPressed(event.getCode());
		});

		rootPane.setOnKeyReleased(event -> {
			logic.keyReleased(event.getCode());
		});

	}

	private void setGameFocus() {
		rootPane.requestFocus();
	}

	public void initScoreListener() {
		scoreLabel.setText(String.valueOf(logic.getScore().getSerializableValue()));

		logic.getScore().addListener((observable, newvalue, oldvalue) -> {
			Platform.runLater(() -> {
				scoreLabel.setText(String.valueOf(observable.getValue()));
			});
		});
	}

	public void simpleLoop() {
		running = true;
		int gameCount = 0;
		while (running) {
			if (gameCount >= logic.getUpdateTime()) {
				logic.gameUpdate();
				gameCount = 0;
			}

			if (logic.getState() == State.RUNNING || logic.getState() == State.DROPPING)
				logic.drawGraphics(blockCanvas.getGraphicsContext2D());
			
			gameCount++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
