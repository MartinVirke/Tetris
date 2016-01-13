package application;

import java.util.Random;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameLogic {

	private Cell[][] cellArray;
	private int cellsInX;
	private int cellsInY;
	// private int score;
	private SimpleStringProperty score;
	private int updateTime, spawnX, spawnY;
	private float unit;
	private Image[] imageArray;
	private Block[] blockArray;
	private Glow glow;
	private GraphicsContext gameGc, nextBlockGc;

	private Block currentBlock;
	private Block nextStepBlock;
	private Block nextBlock;

	private State state;

	private enum Action {
		LEFT, RIGHT, ROTATE, DROP, FALL, SHOW
	}

	private enum State {
		RUNNING, PAUSED, GAMEOVER, ANIMATING, DROPPING
	}

	public GameLogic(Label scoreLabel, GraphicsContext gameGc, GraphicsContext nextBlockGc) {
		super();
		this.cellsInX = 10;
		this.cellsInY = 20;
		this.cellArray = new Cell[cellsInX][cellsInY];
		this.spawnX = 4;
		this.spawnY = 1;
		this.gameGc = gameGc;
		this.nextBlockGc = nextBlockGc;
		score = new SimpleStringProperty("0");
		glow = new Glow();
		glow.setLevel(0.9);
		addImages();
		addBlocks();
		populateArray();
		newBlock();
		spawnBlock();
		setBackground();
		state = State.RUNNING;
		updateTime = 60;
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
		blockArray[0] = new LBlock(spawnX, spawnY, 1);
		blockArray[1] = new RLBlock(spawnX, spawnY, 3);
		blockArray[2] = new SBlock(spawnX, spawnY, 2);
		blockArray[3] = new RSBlock(spawnX, spawnY, 2);
		blockArray[4] = new SquareBlock(spawnX, spawnY, 3);
		blockArray[5] = new TBlock(spawnX, spawnY, 2);
		blockArray[6] = new LineBlock(spawnX, spawnY, 3);
	}

	public synchronized void gameUpdate() {
		// int count = 0;
		// while (isRunning) {
		// try {
		// if (!instantUpdate) {
		// Thread.sleep(20);
		// count++;
		// if (count >= updateTime) {
		if (state != State.DROPPING) {
			updateBlock(Action.FALL);
		}
		// count = 0;
		// }
		// } else {
		// updateBlock(Action.SHOW);
		// count = 0;
		// instantUpdate = false;
		// }

		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// }

		// });
		// gameThread.start();
	}

	private void populateArray() {
		for (int i = 0; i < cellsInY; i++) {
			for (int j = 0; j < cellsInX; j++) {
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
				updateTime = 10;
				break;
			case D:
				updateBlock(Action.RIGHT);
				break;
			case SPACE:
				dropBlock();
				break;
			default:
				break;
			}

	}

	private void dropBlock() {
		state = State.DROPPING;
		
		nextStepBlock = currentBlock.makeCopy();
		deactivateBlockCells(currentBlock);
		nextStepBlock.setY(findLowpoint(nextStepBlock));
		int lowPoint = nextStepBlock.getY();
		Thread tmpThread = new Thread(() -> {
			for (int i = currentBlock.getY(); i <= lowPoint; i++) {
				deactivateBlockCells(nextStepBlock);
				nextStepBlock.setY(i);
				activateBlockCells(nextStepBlock);
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
			spawnBlock();
			state = State.RUNNING;
		});
		tmpThread.start();
	}

	public synchronized void keyReleased(KeyCode code) {
		switch (code) {
		case S:
			updateTime = 100;
			break;
		default:
			break;
		}
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
			cellArray[getCellX(i, block)][getCellY(i, block)].setColorId(block.getColor().ordinal());
			cellArray[getCellX(i, block)][getCellY(i, block)].setAlive(true);
		}
	}

	private void deactivateBlockCells(Block block) {
		for (int i = 0; i < 4; i++) {
			cellArray[getCellX(i, block)][getCellY(i, block)].setAlive(false);
		}
	}

	boolean isValidCell(int index, Block block) {
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
		// newBlock();
		currentBlock = nextBlock;
		newBlock();

		if (!isValidCell(0, currentBlock)) {
			gameOver();
		}
		// instantUpdate = true;
		removeLines();
	}

	private void gameOver() {
		state = State.GAMEOVER;
	}

	private void removeLines() {
		int count = 0;
		int linesCleared = 0;
		for (int i = 0; i < cellsInY; i++) {
			for (int j = 0; j < cellsInX; j++) {
				if (!cellArray[j][i].isAlive())
					break;
				else {
					count++;
				}
				if (count == cellsInX) {
					moveLinesDown(i);
					linesCleared++;
				}
			}
			count = 0;
		}
		if (linesCleared > 0)
			addScore(linesCleared);
	}

	private void addScore(int linesCleared) {
		int oldValue = Integer.parseInt(score.getValue());
		switch (linesCleared) {
		case 1:
			// score += 40;
			score.setValue(String.valueOf(oldValue + 40));
			break;
		case 2:
			score.setValue(String.valueOf(oldValue + 100));
			// score += 100;
			break;
		case 3:
			score.setValue(String.valueOf(oldValue + 300));
			// score += 300;
			break;
		case 4:
			score.setValue(String.valueOf(oldValue + 1200));
			// score += 1200;
			break;
		default:
			break;
		}
	}

	private void moveLinesDown(int startRow) {
		for (int i = startRow; i > 0; i--) {
			for (int j = 0; j < cellsInX; j++) {
				cellArray[j][i].setAlive(cellArray[j][i - 1].isAlive());
				cellArray[j][i].setColorId(cellArray[j][i - 1].getImageId());
			}
		}
	}

	public int getUpdateTime() {
		return updateTime;
	}

	private void setBackground() {
		double cellsX = (double) cellsInX / 2;
		double sumX, sumY, totalsum;
		double di;
		double dj;

		nextBlockGc.getCanvas().setEffect(glow);

		unit = (float) (gameGc.getCanvas().getWidth() / cellsInX);
		gameGc.setStroke(Color.BLACK);

		for (int i = 0; i < cellsInY; i++) {
			for (int j = 0; j < cellsInX; j++) {

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

				cellArray[j][i].setColors(0.15f, 0.15f, 0.8f, totalsum);
				cell.setColors(0.15f, 0.15f, 0.8f, totalsum);

				gameGc.strokeRect(j * unit, i * unit, unit, unit);
				gameGc.setFill(cell.getBgColor());
				gameGc.fillRect(j * unit, i * unit, unit, unit);
				gameGc.setFill(cell.getBgShade());
				gameGc.fillRect(j * unit, i * unit, unit, unit);
			}
		}
	}

	
	public void drawGraphics(GraphicsContext blockGc) {
		blockGc.getCanvas().setEffect(glow);
		blockGc.clearRect(0, 0, blockGc.getCanvas().getWidth(), blockGc.getCanvas().getHeight());
		nextBlockGc.clearRect(0, 0, nextBlockGc.getCanvas().getWidth(), nextBlockGc.getCanvas().getHeight());
		for (int i = 0; i < cellsInY; i++) {
			for (int j = 0; j < cellsInX; j++) {
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public SimpleStringProperty getScore() {
		return score;
	}
}
