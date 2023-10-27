package helpers;

public class Buffer {
	public static final int BUFFER_REALLOC_AMOUNT = 2000;
	
	private Character[] data;
	private int len;
	private int readIndex;
	private int mSize;
	
	public Buffer() {
		this.setData(new Character[BUFFER_REALLOC_AMOUNT]);
		this.setReadIndex(0);
		this.setLen(0);
		this.setMSize(BUFFER_REALLOC_AMOUNT);
	}

	public Character[] getData() {
		return data;
	}

	public void setData(Character[] data) {
		this.data = data;
	}

	public int getReadIndex() {
		return readIndex;
	}

	public void setReadIndex(int readIndex) {
		this.readIndex = readIndex;
	}
	
	public int getLen() {
		return this.len;
	}
	
	public void setLen(int len) {
		this.len = len;
	}
	
	public int getMSize() {
		return this.mSize;
	}
	
	public void setMSize(int mSize) {
		this.mSize = mSize;
	}
	
	public static Buffer bufferCreate() {
		Buffer buf = new Buffer();
		return buf;
	}
	
	public void bufferExtend(int size) {
		Character[] data = new Character[this.getMSize() + size];
		for(int i = 0; i < this.getLen(); i++) {
			data[i] = this.getData()[i];
		}
		this.setData(data);
		this.setMSize(this.getMSize() + size);
	}
	
	public void bufferNeed(int size) {
		if (this.getMSize() <= this.getLen() + size) {
			size += BUFFER_REALLOC_AMOUNT;
			bufferExtend(size);
		}
	}
	
	public void bufferPrintf(String fmt, String... args) {
		int index = this.len;
		// Aktuell haben wir die Limitierung, dass von einer Maximalgröße von 2048 ausgegangen wird.
		int len = 2048;
		this.bufferExtend(len);
		int actualLen = index + len + args.length;
		this.len += actualLen;
	}
	
	public void bufferWrite(char c) {
		this.bufferNeed(1);
		
		this.getData()[this.getLen()] = c;
		this.setLen(this.getLen() + 1);
	}
	
	public String bufferPtr() {
		String output = "";
		for(int i = 0; i < this.len; i++) {
			output += this.getData()[i];
		}
		return output;
	}
	
	public char bufferRead() {
		if (this.readIndex >= this.getLen()) {
			return (char) -1;
		}
		char c = this.data[this.readIndex];
		this.readIndex++;
		return c;
	}
	
	public char bufferPeek() {
		if (this.readIndex >= this.getLen()) {
			return (char) -1;
		}
		char c = this.data[this.readIndex];
		return c;
	}
	
	
}
