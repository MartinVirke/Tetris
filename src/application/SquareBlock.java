package application;

public class SquareBlock extends Block{

	public SquareBlock(int x, int y, int rot) {
		super(x, y, rot);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Block makeCopy() {
		return new SquareBlock(this.getX(), this.getY(), this.getRot());
	}

}
