package application;

public abstract class Block {

	private int x, y, rot;
	protected int[][] pattern;

	public Block(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRot() {
		return rot;
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
		this.rot = rot + 1 > 3 ? 0 : this.rot + 1;
	}

	public int[][] getPattern() {
		return pattern;
	}

}
