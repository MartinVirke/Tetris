package application;

/**
 * Class for creating a reverse L-shaped block.
 * 
 * @author Martin Virke
 */

public class RLBlock extends Block {

	// Auto-generated variable.
	private static final long serialVersionUID = 1L;

	/**
	 * @param x
	 *            X position of the new block.
	 * @param y
	 *            Y position of the new block.
	 * @param rot
	 *            Rotation of the new block.
	 */

	public RLBlock(int x, int y, int rot) {
		super(x, y, rot, Color.GREEN);

		this.pattern = new int[][] { { 0, 0, 0, -1 }, { -1, 0, 1, 1 }, { 0, 0, 0, 1 }, { 1, 0, -1, -1 } };
	}

	/**
	 * Returns a copy of the block.
	 */

	@Override
	public Block makeCopy() {
		return new RLBlock(this.getX(), this.getY(), this.getRot());
	}
}
