package application;

public class RSBlock extends Block{

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	public RSBlock(int x, int y, int rot) {
		super(x, y, rot, Color.TURQUOISE);

		this.pattern = new int[][] { { -1, 0, 0, 1 }, { 0, 0, 1, 1 }, { 1, 0, 0, -1 }, { 0, 0, -1, -1 } };
	}

	@Override
	public Block makeCopy() {
		return new RSBlock(this.getX(), this.getY(), this.getRot());
	}

}
