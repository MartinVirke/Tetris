package application;

public class Cell {

	private int x,y,colorId;
	private boolean alive;
	
	public Cell(int x, int y, int colorId) {
		super();
		this.x = x;
		this.y = y;
		this.colorId = colorId;
	}

	public boolean isAlive() {
		return alive;
	}
	
	

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public void setColorId(int colorId){
		this.colorId = colorId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getColorId() {
		return colorId;
	}
	

}
