package application;

import javafx.scene.input.KeyCode;

public class GameLogic {

	private Cell[][] cellArray;
	private int cellsInX;
	private int cellsInY;
	private boolean isRunning;
	private int updateTime;

	Block currentBlock;
	Block nextStepBlock;

	private Action action;

	private enum Action{
		LEFT, RIGHT, ROTATE, DROP
	}

	public GameLogic() {
		super();
		this.cellsInX = 10;
		this.cellsInY = 20;
		this.cellArray = new Cell[cellsInX][cellsInY];
		populateArray();

		// *****************TEMP*********************
		isRunning = true;
		updateTime = 50;

		Thread thread = new Thread(() -> {
			while (isRunning) {
				for (int i = 0; i < cellsInY; i++) {
					for (int j = 0; j < cellsInX; j++) {
						if (cellArray[j][i].isAlive())
							System.out.print("|X");
						else
							System.out.print("|O");
					}
					System.out.print("|\n");
				}
				System.out.println("");
				try {
					Thread.sleep(1000);
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
		currentBlock = new LBlock(5, 5, 0);
	}

	public void keyPressed(KeyCode code) {

		switch (code) {
		case W:
			// Rotate
			System.out.println("w");
			moveBlock(Action.ROTATE);
//			rotateBlock();
			break;
		case A:
			System.out.println("a");
			moveBlock(Action.LEFT);
//			moveLeft();
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
		}
		
		deactivateBlockCells(currentBlock);
		if (!isValidCell(0, nextStepBlock)) {
			activateBlockCells(currentBlock);
		} else {
			activateBlockCells(nextStepBlock);
			currentBlock = nextStepBlock;
		}

	}
	
//	private void moveLeft() {
//
//		nextStepBlock = currentBlock.makeCopy();
//		nextStepBlock.setX(nextStepBlock.getX() - 1);
//		deactivateBlockCells(currentBlock);
//		if (!isValidCell(0, nextStepBlock)) {
//			activateBlockCells(currentBlock);
//		} else {
//			activateBlockCells(nextStepBlock);
//			currentBlock = nextStepBlock;
//		}
//
//	}

//	private void rotateBlock() {
//		nextStepBlock = currentBlock.makeCopy();
//		nextStepBlock.incRot();
//		deactivateBlockCells(currentBlock);
//		if (!isValidCell(0, nextStepBlock)) {
//			activateBlockCells(currentBlock);
//		} else {
//			activateBlockCells(nextStepBlock);
//			currentBlock = nextStepBlock;
//		}
//	}

	public int getUpdateTime() {
		return updateTime;
	}

	public boolean isRunning() {
		return isRunning;
	}

}
