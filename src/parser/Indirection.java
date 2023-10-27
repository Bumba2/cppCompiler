package parser;

public class Indirection {
	private int pointerDepth;
	
	public Indirection() {};
	
	public Indirection(int pointerDepth) {
		this.pointerDepth = pointerDepth;
	}

	public int getPointerDepth() {
		return pointerDepth;
	}

	public void setPointerDepth(int pointerDepth) {
		this.pointerDepth = pointerDepth;
	}
}
