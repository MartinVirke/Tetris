package application;

/**
 * Class for creating a L-shaped block.
 * 
 * @author Martin Virke
 */

public class LBlock extends Block {

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

	public LBlock(int x, int y, int rot) {
		super(x, y, rot, Color.RED);

		// Gets coords with 1st index of two containers, so show cell at x = 0,
		// y = -1, x = 0, y = 0 etc.
		// The second index needs to be the same and increment each step
		// and the first needs to be arranged like this: 0 - 1, 1 - 2, 2 - 3, 3
		// - 0
		// x = pattern[0][0], y = pattern[1][0]
		// x = pattern[0][1], y = pattern[1][1]
		// x = pattern[0][2], y = pattern[1][2]
		// x = pattern[0][3], y = pattern[1][3]
		// This will draw the complete shape, by incrementing or decrementing
		// the first index a new rotation will be drawn.

		this.pattern = new int[][] { { 0, 0, 0, 1 }, { -1, 0, 1, 1 }, { 0, 0, 0, -1 }, { 1, 0, -1, -1 } };
	}

	// makeCopy() overridar den abstrakta metoden i Block.

	/**
	 * Returns a copy of the block.
	 */

	@Override
	public Block makeCopy() {
		return new LBlock(this.getX(), this.getY(), this.getRot());
	}

}
