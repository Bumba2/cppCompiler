package parserStackFrames;

import parser.Node;
import parserDatatypes.Datatype;

public class StackFrameData<T> {
	private Datatype<Node<T>> dtype;
	
	public StackFrameData(Datatype<Node<T>> dtype) {
		this.dtype = dtype;
	}

	public Datatype<Node<T>> getDtype() {
		return dtype;
	}

	public void setDtype(Datatype<Node<T>> dtype) {
		this.dtype = dtype;
	}
}
