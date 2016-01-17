package application;

import java.io.Serializable;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

	public GameLogic(Pane pauseMenu, GraphicsContext bgGc, Controller controller) {
		super();
		this.bgGc = bgGc;
		this.pauseMenu = pauseMenu;
		this.controller = controller;

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

	public synchronized void gameUpdate() {
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

	public synchronized void keyReleased(KeyCode code) {
		switch (code) {
		case S:
			updateTime = 20;
			break;
		default:
			break;
		}
	}

	public void toggleShowMenu() {
		pauseMenu.setVisible(!pauseMenu.isVisible());
	}

	public void togglePause() {
		setState(state == State.PAUSED ? State.RUNNING : State.PAUSED);
	}

	private void dropBlock() {
		setState(State.DROPPING);
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
					e.printStackTrace();
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

	public boolean isInstantUpdate() {
		return instantUpdate;
	}

	public void setInstantUpdate(boolean instantUpdate) {
		this.instantUpdate = instantUpdate;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
		controller.stateChange(state);
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public SimpleIntegerPropertySerializable getScore() {
		return score;
	}
}
