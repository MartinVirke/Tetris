package application;

public abstract class Block {

	private int x, y, rot;
	protected int[][] pattern;

	public Block(int x, int y, int rot) {
		this.x = x;
		this.y = y;
		this.rot = rot;
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
		this.rot = rot + 1 > 3 ? 0 : rot + 1;
	}
	
	public void setRot(int i){
		this.rot = i;
	}

	public int[][] getPattern() {
		return pattern;
	}

	public abstract Block makeCopy();

}
