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
import javafx.scene.effect.Glow;
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
	private Button resumeBtn, loadBtn, saveBtn, highscoresBtn, exitBtn;

	private GameLogic logic;

	private ReadWriteHandler rwHandler;

	private boolean running;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		logic = new GameLogic(pauseMenu, bgCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());
		rwHandler = new ReadWriteHandler();
		
		blockCanvas.toFront();
		blockCanvas.setEffect(new Glow(0.9));
		nextBlockCanvas.setEffect(new Glow(0.9));
		
		initScoreListener();

		Platform.runLater(() -> {
			rootPane.requestFocus();
		});

		rootPane.focusedProperty().addListener((observable, newvalue, oldvalue) -> {
			Platform.runLater(() -> {
				rootPane.requestFocus();
			});
		});

		scoreLabel.setText(String.valueOf(logic.getScore().getSerializableValue()));

		rootPane.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.09, 1), null, null)));

		resumeBtn.setOnAction(event -> {
			logic.togglePause();
			logic.toggleShowMenu();
		});

		loadBtn.setOnAction(event -> {
			logic = rwHandler.loadFile();
			logic.initFromSave(pauseMenu, bgCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());
			logic.togglePause();
			logic.toggleShowMenu();
			initScoreListener();
		});

		saveBtn.setOnAction(event -> {
			rwHandler.saveFile(logic);
		});

		highscoresBtn.setOnAction(event -> {

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

	public void initScoreListener() {
		scoreLabel.setText(String.valueOf(logic.getScore().getSerializableValue()));
		
		logic.getScore().addListener((observable, newvalue, oldvalue) -> {
			Platform.runLater(() -> {
				scoreLabel.setText(String.valueOf(observable.getValue()));
//						logic.getScore().getSerializableValue()));
			});
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

			if (logic.getState() == State.RUNNING)
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
