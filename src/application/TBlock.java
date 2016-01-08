package application;

public class TBlock extends Block{

	public TBlock(int x, int y, int rot) {
		super(x, y, rot);
		this.pattern = new int[][] { { -1, 0, 1, 0 }, { 0, 0, 0, 1 }, { 1, 0, -1, 0 }, { 0, 0, 0, -1 } };
	}
	

	@Override
	public Block makeCopy() {
		return new TBlock(this.getX(), this.getY(), this.getRot());
	}

}
