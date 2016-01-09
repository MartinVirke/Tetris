package application;

public class SquareBlock extends Block{

	public SquareBlock(int x, int y, int rot) {
		super(x, y, rot, Color.BLUE);

		this.pattern = new int[][] { { -1, 0, 0, -1 }, { -1, -1, 0, 0 }, { 1, 0, 0, 1 }, { 1, 1, 0, 0 } };

	}
	
	@Override
	public Block makeCopy() {
		return new SquareBlock(this.getX(), this.getY(), this.getRot());
	}

}
