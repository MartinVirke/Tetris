package application;

public class Cell {

	private int x,y;
	private boolean alive;
	
	public Cell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
