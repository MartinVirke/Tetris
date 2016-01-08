package application;

public class RSBlock extends Block{

	public RSBlock(int x, int y, int rot) {
		super(x, y, rot);

		this.pattern = new int[][] { { -1, 0, 0, 1 }, { 1, 1, 0, 0 }, { 1, 0, 0, -1 }, { -1, -1, 0, 0 } };
	}

	@Override
	public Block makeCopy() {
		return new RSBlock(this.getX(), this.getY(), this.getRot());
	}

}
