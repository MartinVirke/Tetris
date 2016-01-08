package application;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class GameLogic {

	private Cell[][] cellArray;
	private int cellsInX;
	private int cellsInY;
	private boolean isRunning;
	private int updateTime;
	private float unit;

	Block currentBlock;
	Block nextStepBlock;

	private Action action;

	private enum Action {
		LEFT, RIGHT, ROTATE, DROP, FALL
	}

	public GameLogic() {
		super();
		this.cellsInX = 10;
		this.cellsInY = 20;
		this.cellArray = new Cell[cellsInX][cellsInY];
		populateArray();

		newBlock();
		// *****************TEMP*********************
		isRunning = true;
		updateTime = 50;

		Thread thread = new Thread(() -> {
			while (isRunning) {
				moveBlock(action.FALL);
				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		thread.start();
		// *****************************************

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
			System.out.println("w");
			moveBlock(Action.ROTATE);
			// rotateBlock();
			break;
		case A:
			System.out.println("a");
			moveBlock(Action.LEFT);
			// moveLeft();
			break;
		case S:
			System.out.println("s");
			// Speed up
			break;
		case D:
			System.out.println("d");
			moveBlock(Action.RIGHT);
			// Move right
			break;
		case SPACE:
			System.out.println("space");
			moveBlock(Action.DROP);
			// Instant drop
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
			;
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
			break;
		case FALL:
			nextStepBlock.setY(nextStepBlock.getY() + 1);
			break;
		default:
			break;
		}

		deactivateBlockCells(currentBlock);
		if (!isValidCell(0, nextStepBlock)) {
			activateBlockCells(currentBlock);
			if (action == action.FALL)
				newBlock();
		} else {
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
		}

	}

	// private void moveLeft() {
	//
	// nextStepBlock = currentBlock.makeCopy();
	// nextStepBlock.setX(nextStepBlock.getX() - 1);
	// deactivateBlockCells(currentBlock);
	// if (!isValidCell(0, nextStepBlock)) {
	// activateBlockCells(currentBlock);
	// } else {
	// activateBlockCells(nextStepBlock);
	// currentBlock = nextStepBlock;
	// }
	//
	// }

	// private void rotateBlock() {
	// nextStepBlock = currentBlock.makeCopy();
	// nextStepBlock.incRot();
	// deactivateBlockCells(currentBlock);
	// if (!isValidCell(0, nextStepBlock)) {
	// activateBlockCells(currentBlock);
	// } else {
	// activateBlockCells(nextStepBlock);
	// currentBlock = nextStepBlock;
	// }
	// }

	private void newBlock() {
		Random random = new Random();
		int randomInt = random.nextInt(7);
		switch (randomInt) {
		case 0:
			currentBlock = new LBlock(5,5,0);
			break;
		case 1:
			currentBlock = new RLBlock(5,5,0);
			break;
		case 2:
			currentBlock = new SBlock(5,5,0);
			break;
		case 3:
			currentBlock = new RSBlock(5,5,0);
			break;
		case 4:
			currentBlock = new SquareBlock(5,5,0);
			break;
		case 5:
			currentBlock = new TBlock(5,5,0);
			break;
		case 6:
			currentBlock = new LineBlock(5,5,0);
			break;
		}
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
					if(cellArray[j][i].isAlive())
						gc.fillRect(cellArray[j][i].getX() * unit, cellArray[j][i].getY() * unit, unit, unit);
				}
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
