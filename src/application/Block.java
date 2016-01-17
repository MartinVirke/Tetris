package application;

import java.io.Serializable;

public abstract class Block implements Serializable{

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;
	private int x, y, rot;
	protected int[][] pattern;
	protected Color color;
	
	protected enum Color{
		BLUE, GREEN, LIGHTBLUE, PURPLE, RED, TURQUOISE, YELLOW
	}
	

	public Block(int x, int y, int rot, Color color) {
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.color = color;
		}

	public abstract Block makeCopy();
	
	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRot() {
		return this.rot;
	}

	public int getYRot() {
		return this.rot + 1 > 3 ? 0 : this.rot + 1;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void incRot() {
		this.rot = rot - 1 < 0 ? 3 : rot - 1;
	}
	
	public void setRot(int i){
		this.rot = i;
	}

	public int[][] getPattern() {
		return pattern;
	}


}
