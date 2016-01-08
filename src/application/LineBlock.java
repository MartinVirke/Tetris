package application;

public class LineBlock extends Block{

	public LineBlock(int x, int y, int rot) {
		super(x, y, rot);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Block makeCopy() {
		return new LineBlock(this.getX(), this.getY(), this.getRot());
	}

}
