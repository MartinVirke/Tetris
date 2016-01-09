package application;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class GameLogic {

	private Cell[][] cellArray;
	private int cellsInX;
	private int cellsInY;
	private boolean isRunning, instantUpdate;
	private int updateTime, spawnX, spawnY;
	private float unit;
	Thread gameThread;

	Block currentBlock;
	Block nextStepBlock;

	// private Action action;

	private enum Action {
		LEFT, RIGHT, ROTATE, DROP, FALL
	}

	public GameLogic() {
		super();
		this.cellsInX = 10;
		this.cellsInY = 20;
		this.cellArray = new Cell[cellsInX][cellsInY];
		this.spawnX = 5;
		this.spawnY = 3;
		populateArray();
		gameUpdate();
		newBlock();
		// *****************TEMP*********************
		updateTime = 1000;

		// *****************************************

	}

	private void gameUpdate() {
		gameThread = new Thread(() -> {
			isRunning = true;
			int count = 0;
			while (isRunning) {
				try {
					if (!instantUpdate){
						Thread.sleep(10);
						count++;
						if(count >= 100){
							moveBlock(Action.FALL);
							count = 0;
						}
					}
					else{
						moveBlock(Action.FALL);
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
			// Rotate
			moveBlock(Action.ROTATE);
			break;
		case A:
			moveBlock(Action.LEFT);
			break;
		case S:
			break;
		case D:
			moveBlock(Action.RIGHT);
			break;
		case SPACE:
			moveBlock(Action.DROP);
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

	private void moveBlock(Action action) {

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
				newBlock();
				activateBlockCells(currentBlock);
			}
		} else {
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
			if (action == Action.DROP) {
				newBlock();
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
			System.out.println(nextStepBlock.getY());
		}
		return count;
	}

	private void newBlock() {
		Random random = new Random();
		int randomInt = random.nextInt(7);
		switch (randomInt) {
		case 0:
			currentBlock = new LBlock(spawnX, spawnY, 0);
			break;
		case 1:
			currentBlock = new RLBlock(spawnX, spawnY, 0);
			break;
		case 2:
			currentBlock = new SBlock(spawnX, spawnY, 0);
			break;
		case 3:
			currentBlock = new RSBlock(spawnX, spawnY, 0);
			break;
		case 4:
			currentBlock = new SquareBlock(spawnX, spawnY, 0);
			break;
		case 5:
			currentBlock = new TBlock(spawnX, spawnY, 0);
			break;
		case 6:
			currentBlock = new LineBlock(spawnX, spawnY, 0);
			break;
		}
		
		instantUpdate = true;

	}

	public int getUpdateTime() {
		return updateTime;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void drawGraphics(GraphicsContext gc) {
		unit = (float) (gc.getCanvas().getWidth() / cellsInX);
		while (isRunning) {
			gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
			for (int i = 0; i < cellsInY; i++) {
				for (int j = 0; j < cellsInX; j++) {
					if (cellArray[j][i].isAlive())
						gc.fillRect(cellArray[j][i].getX() * unit, cellArray[j][i].getY() * unit, unit, unit);
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
