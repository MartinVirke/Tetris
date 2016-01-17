package application;

import java.io.Serializable;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * This class is a model, holding the logic that allows the controller class to
 * manipulate a two dimensional array of cells (the playing field), as well as
 * the current state of the game. Most methods are private and only utilized in
 * a step-by-step fashion to properly execute the required steps for
 * manipulating the array of cells.
 * 
 * The class is serializable so that the state of the game can be saved and
 * loaded for later use.
 * 
 * @author Martin Virke
 */

public class GameLogic implements Serializable {

	// Auto-generated variable
	private static final long serialVersionUID = 1L;

	private final int CELLS_IN_X = 10;
	private final int CELLS_IN_Y = 20;
	private final int SPAWN_X = 4;
	private final int SPAWN_Y = 1;

	private int updateTime;
	private float unit;

	private Block currentBlock, nextStepBlock, nextBlock;
	private Block[] blockArray;
	private Cell[][] cellArray;
	private SimpleIntegerPropertySerializable score;
	private State state;

	private transient boolean instantUpdate;
	private transient Controller controller;
	private transient GraphicsContext bgGc;
	private transient Pane pauseMenu;
	private transient Image[] imageArray;

	private enum Action {
		LEFT, RIGHT, ROTATE, DROP, FALL, SHOW
	}

	/**
	 * Sets up everything needed for playing the game by calling the relevant
	 * methods in order.
	 * 
	 * @param pauseMenu
	 *            Reference for showing and hiding the pause menu.
	 * @param bgGc
	 *            Graphics Context for drawing the background at start.
	 * @param controller
	 *            Reference back to the controller instance.
	 */

	public GameLogic(Pane pauseMenu, GraphicsContext bgGc, Controller controller) {
		super();
		this.bgGc = bgGc;
		this.pauseMenu = pauseMenu;
		this.controller = controller;

		// Komposition: GameLogic har celler, och celler har ingen mening
		// utanför GameLogics existens.
		cellArray = new Cell[CELLS_IN_X][CELLS_IN_Y];
		instantUpdate = false;
		score = new SimpleIntegerPropertySerializable(0);
		addImages();
		addBlocks();
		populateArray();
		newBlock();
		spawnBlock();
		setBackground();
		setState(State.RUNNING);
		updateTime = 20;
	}

	/**
	 * 
	 * Sets up everything lost in serialization.
	 * 
	 * @param pauseMenu
	 *            Reference for showing and hiding the pause menu.
	 * @param bgGc
	 *            Graphics Context for drawing the background at start.
	 * @param controller
	 *            Reference back to the controller instance.
	 */

	public void initFromSave(Pane pauseMenu, GraphicsContext bgGc, Controller controller) {
		addImages();
		this.pauseMenu = pauseMenu;
		this.bgGc = bgGc;
		this.controller = controller;
		for (int i = 0; i < CELLS_IN_Y; i++) {
			for (int j = 0; j < CELLS_IN_X; j++) {
				cellArray[j][i].setColors();
			}
		}
	}

	/**
	 * This method is called within a loop to make the block fall after a set
	 * period of time.
	 */

	public void gameUpdate() {
		if (state == State.RUNNING) {
			updateBlock(Action.FALL);
		}
	}

	private void addImages() {
		imageArray = new Image[7];
		imageArray[0] = new Image("Image/blue.jpg");
		imageArray[1] = new Image("Image/green.jpg");
		imageArray[2] = new Image("Image/lightblue.jpg");
		imageArray[3] = new Image("Image/purple.jpg");
		imageArray[4] = new Image("Image/red.jpg");
		imageArray[5] = new Image("Image/turquoise.jpg");
		imageArray[6] = new Image("Image/yellow.jpg");
	}

	private void addBlocks() {
		// bockArray använder polymorfism för att instansiera och förvara olika
		// subklasser till Block i samma array.
		blockArray = new Block[7];
		blockArray[0] = new LBlock(SPAWN_X, SPAWN_Y, 1);
		blockArray[1] = new RLBlock(SPAWN_X, SPAWN_Y, 3);
		blockArray[2] = new SBlock(SPAWN_X, SPAWN_Y, 2);
		blockArray[3] = new RSBlock(SPAWN_X, SPAWN_Y, 2);
		blockArray[4] = new SquareBlock(SPAWN_X, SPAWN_Y, 3);
		blockArray[5] = new TBlock(SPAWN_X, SPAWN_Y, 2);
		blockArray[6] = new LineBlock(SPAWN_X, SPAWN_Y, 3);
	}

	private void populateArray() {
		for (int i = 0; i < CELLS_IN_Y; i++) {
			for (int j = 0; j < CELLS_IN_X; j++) {
				cellArray[j][i] = new Cell(j, i);
			}
		}
	}

	/**
	 * Takes input from the controller class and decides what to do with it.
	 * Input is locked unless the game is running. It is synchronized so that it
	 * cannot be interrupted while processing a user input (causing conflicts in
	 * the logic), since it's coming from a different thread.
	 * 
	 * @param code
	 *            The keycode corresponding to the key press registered by the
	 *            controller.
	 */

	public synchronized void keyPressed(KeyCode code) {
		if (state == State.RUNNING)
			switch (code) {
			case W:
				updateBlock(Action.ROTATE);
				break;
			case A:
				updateBlock(Action.LEFT);
				break;
			case S:
				updateTime = 4;
				break;
			case D:
				updateBlock(Action.RIGHT);
				break;
			case SPACE:
				dropBlock();
				break;
			case ESCAPE:
				togglePause();
				toggleShowMenu();
				break;
			default:
				break;
			}
	}

	/**
	 * Takes input from the controller class and decides what to do with it. It
	 * is synchronized so that it cannot be interrupted while processing a user
	 * input (causing conflicts in the logic), since it's coming from a
	 * different thread.
	 * 
	 * @param code
	 *            The keycode corresponding to the key press registered by the
	 *            controller.
	 */

	public synchronized void keyReleased(KeyCode code) {
		switch (code) {
		case S:
			updateTime = 20;
			break;
		default:
			break;
		}
	}

	/**
	 * Toggles between showing and hiding the pause menu.
	 */

	public void toggleShowMenu() {
		pauseMenu.setVisible(!pauseMenu.isVisible());
	}

	/**
	 * Toggles between pausing and running the game.
	 */

	public void togglePause() {
		setState(state == State.PAUSED ? State.RUNNING : State.PAUSED);
	}

	private void dropBlock() {
		setState(State.DROPPING);
		// currentBlock.makeCopy() kommer genom polymorfism kalla på den
		// overridade metoden för att göra en kopia av samma typ som
		// currentBlock hämtade ur arrayen. Detta eftersom nextStepBlock ska bli
		// samma typ, inte den abstrakta klassen Block som inte går att
		// instansiera,
		nextStepBlock = currentBlock.makeCopy();
		deactivateBlockCells(currentBlock);
		nextStepBlock.setY(findLowpoint(nextStepBlock));
		int lowPoint = nextStepBlock.getY();
		Thread dropThread = new Thread(() -> {
			for (int i = currentBlock.getY(); i <= lowPoint; i++) {
				deactivateBlockCells(nextStepBlock);
				nextStepBlock.setY(i);
				activateBlockCells(nextStepBlock);
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
			spawnBlock();
			setState(State.RUNNING);
		});
		dropThread.start();
	}

	private int getCellX(int index, Block block) {
		int tmpX = block.getPattern()[block.getRot()][index];
		return block.getX() + tmpX;
	}

	private int getCellY(int index, Block block) {
		int tmpY = block.getPattern()[block.getYRot()][index];
		return block.getY() + tmpY;
	}

	private void activateBlockCells(Block block) {
		for (int i = 0; i < 4; i++) {
			cellArray[getCellX(i, block)][getCellY(i, block)].setImageId(block.getColor().ordinal());
			cellArray[getCellX(i, block)][getCellY(i, block)].setAlive(true);
		}
	}

	private void deactivateBlockCells(Block block) {
		for (int i = 0; i < 4; i++) {
			cellArray[getCellX(i, block)][getCellY(i, block)].setAlive(false);
		}
	}

	private boolean isValidCell(int index, Block block) {
		try {
			if (cellArray[getCellX(index, block)][getCellY(index, block)].isAlive()) {
				return false;
			} else if (index < 3) {
				if (isValidCell(index + 1, block)) {
					return true;
				} else
					return false;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}

	private void updateBlock(Action action) {
		// currentBlock.makeCopy() kommer genom polymorfism kalla på den
		// overridade metoden för att göra en kopia av samma typ som
		// currentBlock hämtade ur arrayen. Detta eftersom nextStepBlock ska bli
		// samma typ, inte den abstrakta klassen Block som inte går att
		// instansiera,
		nextStepBlock = currentBlock.makeCopy();
		deactivateBlockCells(currentBlock);
		switch (action) {
		case ROTATE:
			nextStepBlock.incRot();
			break;
		case LEFT:
			nextStepBlock.setX(nextStepBlock.getX() - 1);
			break;
		case RIGHT:
			nextStepBlock.setX(nextStepBlock.getX() + 1);
			break;
		case FALL:
			nextStepBlock.setY(nextStepBlock.getY() + 1);
			break;
		default:
			break;
		}
		if (!isValidCell(0, nextStepBlock)) {
			activateBlockCells(currentBlock);
			if (action == Action.FALL) {
				spawnBlock();
				activateBlockCells(currentBlock);
			}
		} else {
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
		}
	}

	private int findLowpoint(Block block) {
		int count = block.getY();
		boolean run = true;
		while (run) {
			if (isValidCell(0, block)) {
				nextStepBlock.setY(nextStepBlock.getY() + 1);
				count++;
			} else {
				count--;
				break;
			}
		}
		return count;
	}

	private void newBlock() {
		Random random = new Random();
		int randomInt = random.nextInt(7);
		// nextBlock använder polymorfism för att tilldela sig själv till ett
		// objekt i blockArray.
		nextBlock = blockArray[randomInt];
	}

	private void spawnBlock() {
		currentBlock = nextBlock;
		newBlock();
		if (!isValidCell(0, currentBlock)) {
			gameOver();
		}
		instantUpdate = true;
		removeLines();
	}

	private void gameOver() {
		setState(State.GAMEOVER);
	}

	private void removeLines() {
		int count = 0;
		int linesCleared = 0;
		for (int i = 0; i < CELLS_IN_Y; i++) {
			for (int j = 0; j < CELLS_IN_X; j++) {
				if (!cellArray[j][i].isAlive())
					break;
				else {
					count++;
				}
				if (count == CELLS_IN_X) {
					moveLinesDown(i);
					linesCleared++;
				}
			}
			count = 0;
		}
		if (linesCleared > 0)
			addScore(linesCleared);
	}

	private void moveLinesDown(int startRow) {
		for (int i = startRow; i > 0; i--) {
			for (int j = 0; j < CELLS_IN_X; j++) {
				cellArray[j][i].setAlive(cellArray[j][i - 1].isAlive());
				cellArray[j][i].setImageId(cellArray[j][i - 1].getImageId());
			}
		}
	}

	private void addScore(int linesCleared) {
		switch (linesCleared) {
		case 1:
			score.setSerializableValue(score.getSerializableValue() + 40);
			break;
		case 2:
			score.setSerializableValue(score.getSerializableValue() + 100);
			break;
		case 3:
			score.setSerializableValue(score.getSerializableValue() + 300);
			break;
		case 4:
			score.setSerializableValue(score.getSerializableValue() + 1200);
			break;
		default:
			break;
		}
	}

	private void setBackground() {
		double cellsX = (double) CELLS_IN_X / 2;
		double sumX, sumY, totalsum;
		double di;
		double dj;

		unit = (float) (bgGc.getCanvas().getWidth() / CELLS_IN_X);
		bgGc.setStroke(Color.BLACK);

		for (int i = 0; i < CELLS_IN_Y; i++) {
			for (int j = 0; j < CELLS_IN_X; j++) {

				Cell cell = cellArray[j][i];

				di = (double) i;
				dj = (double) j;
				sumY = di;

				if (cellsX < dj) {
					dj = dj - cellsX;
					sumX = cellsX - dj;
				} else if (cellsX > dj) {
					sumX = dj + 1;
				} else {
					sumX = dj;
				}

				totalsum = sumX + sumY;

				cell.setColors(0.2f, 0.2f, 0.8f, totalsum);

				bgGc.strokeRect(j * unit, i * unit, unit, unit);
				bgGc.setFill(cell.getBgColor());
				bgGc.fillRect(j * unit, i * unit, unit, unit);
				bgGc.setFill(cell.getBgShade());
				bgGc.fillRect(j * unit, i * unit, unit, unit);
			}
		}
	}

	/**
	 * Handles the conversion from the logic to a graphical representation of
	 * the game. It scales the array of cells so every index can be properly
	 * displayed on a JavaFX canvas.
	 * 
	 * @param blockGc
	 *            The GraphicsContext for the blocks.
	 * @param nextBlockGc
	 *            The GraphicsContext for displaying the next block coming up.
	 */

	public void drawGraphics(GraphicsContext blockGc, GraphicsContext nextBlockGc) {
		blockGc.clearRect(0, 0, blockGc.getCanvas().getWidth(), blockGc.getCanvas().getHeight());
		nextBlockGc.clearRect(0, 0, nextBlockGc.getCanvas().getWidth(), nextBlockGc.getCanvas().getHeight());
		for (int i = 0; i < CELLS_IN_Y; i++) {
			for (int j = 0; j < CELLS_IN_X; j++) {
				if (cellArray[j][i].isAlive()) {
					blockGc.drawImage(imageArray[cellArray[j][i].getImageId()], j * unit, i * unit, unit, unit);
					blockGc.setFill(cellArray[j][i].getBgShade());
					blockGc.fillRect(j * unit, i * unit, unit, unit);
				}
			}
		}
		for (int i = 0; i < 4; i++) {
			nextBlockGc.drawImage(imageArray[nextBlock.getColor().ordinal()],
					(nextBlock.getPattern()[nextBlock.getRot()][i] * unit) + unit,
					(nextBlock.getPattern()[nextBlock.getYRot()][i] * unit) + unit, unit, unit);
		}
	}

	/**
	 * This method will be checked to see if logic needs to updated instantly.
	 * This is needed when dropping a block using spacebar, since the logic will
	 * otherwise update whenever the old timer has run out. This can be any step
	 * between 0 and the maximum steps allowed, thus causing irregular intervals
	 * between dropping a block and the new block being drawn.
	 * 
	 * @return instantUpdate
	 */

	public boolean isInstantUpdate() {
		return instantUpdate;
	}

	/**
	 * This method will be called every time the logic needs to updated
	 * instantly. This is needed when dropping a block using spacebar, since the
	 * logic will otherwise update whenever the old timer has run out. This can
	 * be any step between 0 and the maximum steps allowed, thus causing
	 * irregular intervals between dropping a block and the new block being
	 * drawn.
	 * 
	 * @param instantUpdate
	 */

	public void setInstantUpdate(boolean instantUpdate) {
		this.instantUpdate = instantUpdate;
	}

	/**
	 * Returns the current state of the game.
	 * 
	 * @return state The state of the game.
	 */

	public State getState() {
		return state;
	}

	/**
	 * Sets the state of the game.
	 * 
	 * @param state
	 *            The state the game should be in.
	 */

	public void setState(State state) {
		this.state = state;
		controller.stateChange(state);
	}

	/**
	 * @return updateTime The time it takes for a block to fall.
	 */

	public int getUpdateTime() {
		return updateTime;
	}

	/**
	 * Returns the current score. It is a SimpleIntegerPropertySerializable
	 * because the controller class adds a listener to it.
	 * 
	 * @return score The current score of the game.
	 */

	public SimpleIntegerPropertySerializable getScore() {
		return score;
	}
}
