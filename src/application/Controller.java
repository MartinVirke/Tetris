package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Controller implements Initializable {

	private final String SAVEFILE_FILENAME = "save.ser";

	private final String HIGHSCORE_FILENAME = "highscore.ser";

	@FXML
	private Pane rootPane, positionalPane, pauseMenu;;
	@FXML
	private Canvas blockCanvas, bgCanvas, nextBlockCanvas;
	@FXML
	private Label scoreLabel, hsLabel, goLabel;
	@FXML
	private VBox vBox1;
	@FXML
	private HBox hBox1;
	@FXML
	private Button resumeBtn, loadBtn, saveBtn, highscoresBtn, exitBtn, hsBtn, goBtn, newGameBtn;
	@FXML
	private BorderPane hsMenu, goMenu;
	@FXML
	private TextField goField;

	private GameLogic logic;

	private ReadWriteHandler rwHandler;

	private HighscoreHandler hsHandler;

	private boolean running;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		logic = new GameLogic(pauseMenu, bgCanvas.getGraphicsContext2D(), this);
		rwHandler = new ReadWriteHandler();
		hsHandler = new HighscoreHandler();

		rootPane.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.09, 1), null, null)));
		pauseMenu.setBackground(new Background(new BackgroundFill(new Color(0.0, 0.0, 0.09, 1), null, null)));
		hsMenu.setBackground(new Background(new BackgroundFill(new Color(0.5, 0.5, 0.9, 1), null, null)));
		goMenu.setBackground(new Background(new BackgroundFill(new Color(0.5, 0.5, 0.9, 1), null, null)));

		blockCanvas.setEffect(new Glow(0.9));
		nextBlockCanvas.setEffect(new Glow(0.9));

		scoreLabel.setText(String.valueOf(logic.getScore().getSerializableValue()));
		hsLabel.setText(hsHandler.getListString());
		
		blockCanvas.toFront();
		hsMenu.toFront();
		goMenu.toFront();

		initScoreListener();

		Platform.runLater(() -> {
			setGameFocus();
			positionalPane.translateXProperty()
					.bind(rootPane.widthProperty().divide(2).subtract(positionalPane.getWidth() / 2));
			positionalPane.translateYProperty()
					.bind(rootPane.heightProperty().divide(2).subtract(positionalPane.getHeight() / 2));
		});

		resumeBtn.setOnAction(event -> {
			logic.togglePause();
			logic.toggleShowMenu();
		});

		newGameBtn.setOnAction(event -> {
			logic = new GameLogic(pauseMenu, bgCanvas.getGraphicsContext2D(), this);
			logic.drawGraphics(blockCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());
			initScoreListener();
			pauseMenu.setVisible(false);
			resumeBtn.setDisable(false);
			saveBtn.setDisable(false);
		});

		loadBtn.setOnAction(event -> {
			logic = (GameLogic) rwHandler.readFiles(logic, SAVEFILE_FILENAME);
			logic.initFromSave(pauseMenu, bgCanvas.getGraphicsContext2D(), this);
			initScoreListener();
			logic.drawGraphics(blockCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());
		});

		saveBtn.setOnAction(event -> {
			rwHandler.writeFile(logic, SAVEFILE_FILENAME);
		});

		goBtn.setOnAction(event -> {
			hsHandler.addEntry(goField.getText(), logic.getScore().intValue());
			goMenu.setVisible(false);
			pauseMenu.setVisible(true);
			rwHandler.writeFile(hsHandler.getHighscoreList(), HIGHSCORE_FILENAME);
		});

		highscoresBtn.setOnAction(event -> {
			// This row is suppressed because casting ArrayList to object is
			// unsafe, however using this approach we can keep hsHandler as
			// open-ended as possible.
			hsHandler.setHighscoreList(
					(ArrayList<HighscoreEntry>) rwHandler.readFiles(hsHandler.getHighscoreList(), HIGHSCORE_FILENAME));
			hsLabel.setText(hsHandler.getListString());
			hsMenu.setVisible(!hsMenu.isVisible());
		});

		hsBtn.setOnAction(event -> {
			hsMenu.setVisible(!hsMenu.isVisible());
			pauseMenu.setVisible(true);
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

		Thread graphicsThread = new Thread(() -> {
			simpleLoop();
		});

		graphicsThread.setName("graphicsThread");
		graphicsThread.start();
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
			if (logic.isInstantUpdate()) {
				gameCount = logic.getUpdateTime();
				logic.setInstantUpdate(false);
			}
			if (gameCount >= logic.getUpdateTime()) {
				logic.gameUpdate();
				gameCount = 0;
			}

			if (logic.getState() == State.RUNNING || logic.getState() == State.DROPPING)
				logic.drawGraphics(blockCanvas.getGraphicsContext2D(), nextBlockCanvas.getGraphicsContext2D());

			gameCount++;
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stateChange(State state) {
		switch (state) {
		case GAMEOVER:
			handleHighscore();
			resumeBtn.setDisable(true);
			saveBtn.setDisable(true);
			break;
		case RUNNING:
			setGameFocus();
			break;
		default:
			break;
		}
	}

	private void handleHighscore() {
		if (hsHandler.isHighscore(logic.getScore().intValue())) {
			goMenu.setVisible(true);
		} else {
			hsMenu.setVisible(true);
		}
	}
}
