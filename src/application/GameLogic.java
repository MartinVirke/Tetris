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
			rotateBlock();
			break;
		case A:
			System.out.println("a");
			// Move left
			break;
		case S:
			System.out.println("s");
			// Speed up
			break;
		case D:
			System.out.println("d");
			// Move right
			break;
		case SPACE:
			System.out.println("space");
			// Instant drop
			break;
		default:
			break;
		}

	}


	private Cell getAffectedCell(int index) {
		int tmpX = currentBlock.getPattern()[currentBlock.getRot()][index];
		int tmpY = currentBlock.getPattern()[currentBlock.getYRot()][index];
		return cellArray[currentBlock.getX() + tmpX][currentBlock.getY() + tmpY];
	}

	private void activateBlockCells() {
		for (int i = 0; i < 4; i++) {
			getAffectedCell(i).setAlive(true);
		}
	}

	private void deactivateBlockCells() {
		for (int i = 0; i < 4; i++) {
			getAffectedCell(i).setAlive(false);
		}
	}

	boolean isAlive(int index) {
		if (getAffectedCell(index).isAlive()) {
			return true;
		} else if (index < 3) {
			isAlive(index + 1);
		}
		return false;
	}

	private void rotateBlock() {
		// ****TEMP****
		// ************
		// nextStepBlock = currentBlock.makeCopy();
		// nextStepBlock.incRot();
		// System.out.println(nextStepBlock.getRot() + " nextStep");

		deactivateBlockCells();
		currentBlock.incRot();
		for (int i = 0; i < 4; i++) {
			if (isAlive(0)) {
				activateBlockCells();
				break;
			}
		}
		activateBlockCells();

		// if (getAffectedCell(currentBlock, i).getX() !=
		// getAffectedCell(nextStepBlock, i).getX()
		// && getAffectedCell(currentBlock, i).getY() !=
		// getAffectedCell(nextStepBlock, i).getY()) {
		// if (!isOccupied(getAffectedCell(nextStepBlock, i))) {
		// } else {
		// for (int j = 0; j < i; j++) {
		// deactivateBlockCells(getAffectedCell(nextStepBlock, j));
		// activateBlockCells(getAffectedCell(currentBlock, j));
		// }
		// currentBlock = nextStepBlock;
		// break;
		// }
		// }

		// currentBlock = nextStepBlock;

	}

	public int getUpdateTime() {
		return updateTime;
	}

	public boolean isRunning() {
		return isRunning;
	}

}
