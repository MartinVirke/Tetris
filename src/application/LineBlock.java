package application;

public class LineBlock extends Block{

	public LineBlock(int x, int y, int rot) {
		super(x, y, rot);

		this.pattern = new int[][] { { 0, 0, 0, 0 }, { -2, -1, 0, 1 }, { 0, 0, 0, 0 }, { 2, 1, 0, -1 } };
	}
	
	@Override
	public Block makeCopy() {
		return new LineBlock(this.getX(), this.getY(), this.getRot());
	}

}
