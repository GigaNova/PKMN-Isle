package Model;

public enum Permission {
	
	ONE(0, 16),
	FOUR(0, 64),
	C(0, 192);
	
	private final int x;
	private final int y;

	Permission(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
