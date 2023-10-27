package parserStackFrames;

public class StackFrameElement {
	private int flags; // Stack-Frame-Element Flag.
	private StackFrameElementType type; // Typ des Frame-Elements.
	// Name des Frame-Elements (darf kein Variablenname sein), zum Beispiel "result_value".
	private String name;
	private int offsetFromBp; // Welches Offset ist dieses Frame-Element vom BasePointer?
	
	public StackFrameElement(int flags, StackFrameElementType type, String name, int offsetFromBp) {
		this.flags = flags;
		this.type = type;
		this.name = name;
		this.offsetFromBp = offsetFromBp;
	}
	
	public StackFrameElement(StackFrameElementType type, String name) {
		this.flags = 0b00000000;
		this.type = type;
		this.name = name;
		this.offsetFromBp = 0b00000000;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public StackFrameElementType getType() {
		return type;
	}
	
	public void setType(StackFrameElementType type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getOffsetFromBp() {
		return offsetFromBp;
	}
	
	public void setOffsetFromBp(int offsetFromBp) {
		this.offsetFromBp = offsetFromBp;
	}
}
