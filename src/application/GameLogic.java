package application;

import java.util.Random;

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
	private int score;
	private boolean isRunning, instantUpdate;
	private int updateTime, spawnX, spawnY;
	private float unit;
	private Image[] imageArray;
	private Label scoreLabel;
	Thread gameThread;
	GraphicsContext gameGc, nextBlockGc, blockGc;

	Block currentBlock;
	Block nextStepBlock;
	Block nextBlock;

	// private Action action;

	private enum Action {
		LEFT, RIGHT, ROTATE, DROP, FALL, SHOW
	}

	public GameLogic(Label scoreLabel, GraphicsContext gameGc, GraphicsContext nextBlockGc, GraphicsContext blockGc) {
		super();
		this.cellsInX = 10;
		this.cellsInY = 20;
		this.cellArray = new Cell[cellsInX][cellsInY];
		this.spawnX = 4;
		this.spawnY = 1;
		this.scoreLabel = scoreLabel;
		this.gameGc = gameGc;
		this.nextBlockGc = nextBlockGc;
		this.blockGc = blockGc;
		addImages();
		populateArray();
		gameUpdate();
		newBlock();
		spawnBlock();
		updateTime = 100;
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

	private void gameUpdate() {
		gameThread = new Thread(() -> {
			isRunning = true;
			int count = 0;
			while (isRunning) {
				try {
					if (!instantUpdate) {
						Thread.sleep(10);
						count++;
						if (count >= updateTime) {
							updateBlock(Action.FALL);
							count = 0;
						}
					} else {
						// moveBlock(Action.FALL);
						updateBlock(Action.SHOW);
						count = 0;
						instantUpdate = false;
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		gameThread.start();
	}

	private void populateArray() {
		for (int i = 0; i < cellsInY; i++) {
			for (int j = 0; j < cellsInX; j++) {
				cellArray[j][i] = new Cell(j, i);
			}
		}
	}

	public void keyPressed(KeyCode code) {

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
			updateBlock(Action.DROP);
			break;
		default:
			break;
		}

	}

	public void keyReleased(KeyCode code) {
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
			;
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
		case DROP:
			nextStepBlock.setY(findLowpoint(nextStepBlock));
			break;
		case FALL:
			nextStepBlock.setY(nextStepBlock.getY() + 1);
			break;
		default:
			break;
		}

		if (!isValidCell(0, nextStepBlock) && action != Action.DROP) {
			activateBlockCells(currentBlock);
			if (action == Action.FALL) {
				spawnBlock();
				activateBlockCells(currentBlock);
			}
		} else {
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
			if (action == Action.DROP) {
				spawnBlock();
			}
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
		switch (randomInt) {
		case 0:
			nextBlock = new LBlock(spawnX, spawnY, 1);
			break;
		case 1:
			nextBlock = new RLBlock(spawnX, spawnY, 3);
			break;
		case 2:
			nextBlock = new SBlock(spawnX, spawnY, 2);
			break;
		case 3:
			nextBlock = new RSBlock(spawnX, spawnY, 2);
			break;
		case 4:
			nextBlock = new SquareBlock(spawnX, spawnY, 3);
			break;
		case 5:
			nextBlock = new TBlock(spawnX, spawnY, 2);
			break;
		case 6:
			nextBlock = new LineBlock(spawnX, spawnY, 3);
			break;
		}

	}

	private void spawnBlock() {
		// newBlock();
		currentBlock = nextBlock;
		newBlock();

		if (!isValidCell(0, currentBlock)) {
			gameOver();
		}
		instantUpdate = true;
		removeLines();
	}

	private void gameOver() {
		isRunning = false;
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
		addScore(linesCleared);
	}

	private void addScore(int linesCleared) {
		switch (linesCleared) {
		case 1:
			score += 40;
			break;
		case 2:
			score += 100;
			break;
		case 3:
			score += 300;
			break;
		case 4:
			score += 1200;
			break;
		default:
			break;
		}
		scoreLabel.setText(String.valueOf(score));
	}

	private void moveLinesDown(int startRow) {
		for (int i = startRow; i > 0; i--) {
			for (int j = 0; j < cellsInX; j++) {
				cellArray[j][i].setAlive(cellArray[j][i - 1].isAlive());
				cellArray[j][i].setColorId(cellArray[j][i - 1].getColorId());
			}
		}
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void drawGraphics() {
		unit = (float) (gameGc.getCanvas().getWidth() / cellsInX);

		Random random = new Random();
		double cellsY = (double) cellsInY / 2;
		double cellsX = (double) cellsInX / 2;
		double sumX, sumY, totalsum;
		double di;
		double dj;

		while (isRunning) {
			gameGc.clearRect(0, 0, gameGc.getCanvas().getWidth(), gameGc.getCanvas().getHeight());
			nextBlockGc.clearRect(0, 0, nextBlockGc.getCanvas().getWidth(), nextBlockGc.getCanvas().getHeight());
			for (int i = 0; i < cellsInY; i++) {
				for (int j = 0; j < cellsInX; j++) {

					// double rand = (double) (random.nextInt(4) + 1) / 2;

					di = (double) i;
					dj = (double) j;

					if (cellsY < di) {
						di = di - cellsY;
						sumY = cellsY - di;
					} else if (cellsY > di) {
						sumY = di + 1;
					} else {
						sumY = di;
					}

					if (cellsX < dj) {
						dj = dj - cellsX;
						sumX = cellsX - dj;
					} else if (cellsX > dj) {
						sumX = dj + 1;
					} else {
						sumX = dj;
					}

					totalsum = sumX + sumY + 3.0f;

					if (cellArray[j][i].isAlive()) {
						blockGc.drawImage(imageArray[cellArray[j][i].getColorId()], j * unit, i * unit, unit, unit);
						blockGc.setFill(new Color(0, 0, 0, 1.0f - totalsum / 20));
						blockGc.fillRect(j * unit, i * unit, unit, unit);
						Glow glow = new Glow();
						glow.setLevel(0.9);
						blockGc.getCanvas().setEffect(glow);

					} else {
						// totalsum += rand;
						gameGc.setFill(new Color(totalsum / 100, totalsum / 100, totalsum / 20, 1));
						gameGc.setStroke(Color.BLACK);
						gameGc.fillRect(j * unit, i * unit, unit, unit);
						gameGc.strokeRect(j * unit, i * unit, unit, unit);
					}
				}
			}

			for (int i = 0; i < 4; i++) {
				nextBlockGc.drawImage(imageArray[nextBlock.getColor().ordinal()],
						(nextBlock.getPattern()[nextBlock.getRot()][i] * unit) + unit,
						(nextBlock.getPattern()[nextBlock.getYRot()][i] * unit) + unit, unit, unit);
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
