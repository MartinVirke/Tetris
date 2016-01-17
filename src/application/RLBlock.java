package application;

public class RLBlock extends Block {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	public RLBlock(int x, int y, int rot) {
		super(x, y, rot, Color.GREEN);

		this.pattern = new int[][] { { 0, 0, 0, -1 }, { -1, 0, 1, 1 }, { 0, 0, 0, 1 }, { 1, 0, -1, -1 } };
	}

	@Override
	public Block makeCopy() {
		return new RLBlock(this.getX(), this.getY(), this.getRot());
	}
}
