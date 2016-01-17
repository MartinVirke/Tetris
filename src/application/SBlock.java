package application;

public class SBlock extends Block{

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	public SBlock(int x, int y, int rot) {
		super(x, y, rot, Color.PURPLE);
		this.pattern = new int[][] { { -1, 0, 0, 1 }, { 1, 1, 0, 0 }, { 1, 0, 0, -1 }, { -1, -1, 0, 0 } };
	}

	@Override
	public Block makeCopy() {
		return new SBlock(this.getX(), this.getY(), this.getRot());
	}

}
