package helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Vector<T> {
	public static final int VECTOR_FLAG_PEEK_DECREMENT = 0b00000001;
	public static final int VECTOR_ELEMENT_INCREMENT = 20;
	
	private ArrayList<T> data;
	// Der Peek-Index der Index, dessen Element ausgegeben wird, wenn "vectorPeek()" aufgerufen wird.
	private int mIndex;
	private int peekIndex;
	private int readIndex;
	private int count;
	private int flags;
	private Vector<T> saves;
	
	public Vector() {
		this.data = new ArrayList<T>(VECTOR_ELEMENT_INCREMENT);
		this.setMIndex(VECTOR_ELEMENT_INCREMENT);
		this.setReadIndex(0);
		this.setPeekIndex(0);
		this.setCount(0);
		this.saves = null;
	}
	
	public ArrayList<T> getData() {
		return this.data;
	}
	
	public void setData(ArrayList<T> data) {
		this.data = data;
	}
	
	public int getMIndex() {
		return this.mIndex;
	}
	
	public void setMIndex(int mIndex) {
		this.mIndex = mIndex;
	}
	
	public int getPeekIndex() {
		return peekIndex;
	}
	
	public void setPeekIndex(int peekIndex) {
		this.peekIndex = peekIndex;
	}
	
	public int getReadIndex() {
		return readIndex;
	}
	
	public void setReadIndex(int readIndex) {
		this.readIndex = readIndex;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getFlags() {
		return flags;
	}
	
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public Vector<T> getSaves() {
		return saves;
	}
	
	public void setSaves(Vector<T> saves) {
		this.saves = saves;
	}
	
	public boolean vectorInBoundsForAt(int index) {
		return (index >= 0 && index < this.readIndex);
	}
	
	public boolean vectorInBoundsForPop(int index) {
		return (index >= 0 && index < this.mIndex);
	}
	
	public void vectorAssertBoundsForPop(int index) {
		assert(this.vectorInBoundsForPop(index));
	}
	
	public Vector<T> vectorCreateNoSaves() {
		Vector<T> vector = new Vector<T>();
		return vector;
	}
	
	public Vector<T> vectorClone() {
		Vector<T> vector = new Vector<T>();
		vector.setCount(this.getCount());
		ArrayList<T> data = new ArrayList<T>(this.getCount());
		for(int i = 0; i < this.getCount(); i++) {
			data.set(i, this.getData().get(i));
		}
		vector.setData(data);
		vector.setFlags(this.getFlags());
		vector.setPeekIndex(this.getPeekIndex());
		vector.setReadIndex(this.getReadIndex());
		return vector;
	}
	
	public static <T> Vector<T> vectorCreate() {
		Vector<T> vec = new Vector<T>();
		vec.saves = new Vector<T>();
		return vec;
	}
	
	public int vectorCurrentIndex() {
		return this.getReadIndex();
	}
	
	public void vectorResizeForIndex(int startIndex, int totalElements) {
		if (startIndex + totalElements < this.mIndex) {
			return; // Es gibts nichts zum Resizen
		}
		ArrayList<T> data = new ArrayList<T>(startIndex + totalElements + VECTOR_ELEMENT_INCREMENT);
		for(int i = 0; i < startIndex; i++) {
			data.add(i, this.getData().get(i));
		}
		this.setMIndex(startIndex + totalElements);
		this.setData(data);
	}
	
	public void vectorResizeFor(int totalElements) {
		this.vectorResizeForIndex(this.getReadIndex(), totalElements);
	}
	
	public void vectorResize() {
		// Null Elemente, um zu prüfen, ob wir den Index schon überfüllt haben
		this.vectorResizeFor(0);
	}
	
	public T vectorAt(int index) {
		return this.data.get(index);
	}
	
	public void vectorSetPeekPointer(int index) {
		this.setPeekIndex(index);
	}
	
	public void vectorSetPeekPointerEnd() {
		this.vectorSetPeekPointer(this.readIndex - 1);
	}
	
	public T vectorPeekAt(int index) {
		if (this.vectorInBoundsForAt(index) == false) {
			return null;
		}
		T ptr = this.vectorAt(index);
		return ptr;
	}
	
	public T vectorPeekNoIncrement() {
		if (this.vectorInBoundsForAt(this.getPeekIndex()) == false) {
			return null;
		}
		T ptr = this.vectorAt(this.getPeekIndex());
		return ptr;
	}
	
	public void vectorPeekBack() {
		this.peekIndex--;
	}
	
	public T vectorPeek() {
		T ptr = this.vectorPeekNoIncrement();
		if (ptr == null) {
			return null;
		}
		// Bitwise AND
		if ((this.flags & Vector.VECTOR_FLAG_PEEK_DECREMENT) == 1) {
			this.peekIndex--;
		}
		else {
			this.peekIndex++;
		}
		return ptr;
	}
	
	public void setFlag(int flag) {
		// Bitwise OR
		this.flags |= flag;
	}
	
	public void unsetFlag(int flag) {
		// Bitwise AND and Bitwise Complement
		this.flags &= ~flag;
	}
	
	public T vectorPeekPtr() {
		T ptr = this.vectorPeek();
		if (ptr == null) {
			return null;
		}
		return ptr;
	}
	
	public T vectorPeekPtrAt(int index) {
		if (index < 0 || index > this.count) {
			return null;
		}
		T ptr = vectorAt(index);
		if (ptr == null) {
			return null;
		}
		return ptr;
	}
	
	public T vectorBackPtr() {
		T ptr = this.vectorBack();
		if (ptr == null) {
			return null;
		}
		return ptr;
	}
	
	public void vectorSave() {
		// Speichert den Status des Vectors zu ihm selbst
		Vector<T> tmpVec = this;
		// Wir dürfen die Saves nicht verändern, also setzen wir sie auf null,
		// wenn wir ihn auf den Save-Stack pushen
		tmpVec.setSaves(null);
		this.setSaves(tmpVec); // Ausprobieren ob es das richtige tut
	}
	
	private void copy(Vector<T> saveVec) {
		this.setCount(saveVec.getCount());
		ArrayList<T> data = new ArrayList<T>(this.count);
		for(int i = 0; i < saveVec.getData().size(); i++) {
			data.set(i, saveVec.getData().get(i));
		}
		this.setData(data);
		this.setFlags(saveVec.getFlags());
		this.setPeekIndex(saveVec.getPeekIndex());
		this.setReadIndex(saveVec.getReadIndex());
	}
	
	public void vectorRestore() {
		Vector<T> saveVec = this.getSaves();
		this.copy(saveVec);
		this.setSaves(this);
		this.getSaves().vectorPop();
	}
	
	public void vectorSavePurge() {
		if (this.getSaves() != null) this.getSaves().vectorPop();
	}
	
	public void vectorPopLastPeek() {
		assert(this.getPeekIndex() >= 1);
		this.vectorPopAt(this.getPeekIndex() - 1);
	}
	
	private boolean setValueAt(int index, T elem) {
		if (index < 0 || index > this.count) {
			return false;
		}
		// Nochmal nachschauen ob set doch möglich ist zu nutzen
		this.data.add(index, elem);
		return true;
	}
	
	public void vectorPush(T elem) {
		int indexToChange = this.getReadIndex();
		this.setValueAt(indexToChange, elem);
		
		this.readIndex++;
		this.count++;
		
		if (this.getReadIndex() >= this.getMIndex()) {
			this.vectorResize();
		}
	}
	
	@SuppressWarnings("unchecked")
	public int vectorFread(int amount, File fp) throws FileNotFoundException, IOException, ClassCastException {
		BufferedReader initialStream = new BufferedReader (new InputStreamReader (new FileInputStream(fp)));
		Character readAmount = (char) initialStream.read();
		while(readAmount != null) {
			this.vectorPush((T) readAmount);
			readAmount = (char) initialStream.read();
		}
		initialStream.close();
		return 0;	
	}

	public String vectorString() {
		String output = "";
		for(int i = 0; i < this.readIndex; i++) {
			output += this.getData().get(i).toString();
		}
		return output;
	}
	
	public T vectorDataEnd() {
		return this.getData().get(this.readIndex);
	}
	
	public int vectorElementsUntilEnd(int index) {
		return this.getCount() - index;
	}
	
	public void vectorShiftRightInBoundsNoIncrement(int index, int amount) {
		this.vectorResizeForIndex(index, amount);
		int eindex = (index + amount);
		this.setValueAt(eindex, this.vectorAt(index));
		for(int i = index + 1; i < eindex; i++) {
			this.getData().set(i, this.vectorAt(index));
		}
	}
	
	public void vectorShiftRightInBounds(int index, int amount) {
		this.vectorShiftRightInBoundsNoIncrement(index, amount);
		this.readIndex += amount;
		this.count += amount;
	}
	
	public void vectorStretch(int index) {
		if (index < this.readIndex) {
			return;
		}
		this.vectorResizeForIndex(index, 0);
		this.setCount(index);
		this.setReadIndex(index);
	}
	
	public int vectorPopValue(T val) {
		int oldPp = this.peekIndex;
		this.vectorSetPeekPointer(0);
		T ptr = this.vectorPeekPtr();
		int index = 0;
		while(ptr != null) {
			if (ptr.equals(val)) {
				this.vectorPopAt(index);
				break;
			}
			ptr = this.vectorPeekPtr();
			index++;
		}
		this.vectorSetPeekPointer(oldPp);
		return index;
	}
	
	public void vectorShiftRight(int index, int amount) {
		if (index < this.readIndex) {
			this.vectorShiftRightInBounds(index, amount);
			return;
		}
		// Wir brauchen nichts verschieben, da wir out of Bounds sind
		// Nun stretchen wir den Vector bis index + amount
		this.vectorStretch(index);
		this.vectorShiftRightInBoundsNoIncrement(index, amount);
	}
	
	public void vectorPopAt(int index) {
		int dstPos = index;
		int nextElementPos = index + 1;
		int endPos = this.readIndex;
		this.setValueAt(dstPos, this.getData().get(nextElementPos));
		for(int i = nextElementPos; i < endPos - 1; i++) {
			this.setValueAt(i, this.getData().get(i + 1));
		}
		this.setCount(this.getCount() - 1);
		this.setReadIndex(this.getReadIndex() - 1);
	}
	
	public void vectorPeekPop() {
		this.vectorPopAt(this.peekIndex);
	}
	
	public void vectorPushMultipleAt(int dstIndex, T ptr, int total) {
		this.vectorShiftRight(dstIndex, total);
		this.setValueAt(dstIndex, ptr);
	}
	
	public void vectorPushAt(int index, T ptr) {
		this.vectorShiftRight(index, 1);
		this.setValueAt(index, ptr);
	}
	
	public int vectorInsert(Vector<T> vectorSrc, int dstIndex) {
		if (this.getData().size() != vectorSrc.getData().size()) {
			return -1;
		}
		this.vectorPushMultipleAt(dstIndex, vectorSrc.vectorAt(0), vectorSrc.vectorCount());
		return 0;
	}
	
	public void vectorPop() {
		// Das Poppen des Vectors dekrementiert den Index
		this.getData().remove(this.readIndex - 1);
		this.readIndex--;
		this.count--;
		this.vectorAssertBoundsForPop(this.readIndex);
	}
	
	public ArrayList<T> vectorDataPtr() {
		return this.getData();
	}
	
	public boolean vectorEmpty() {
		return vectorCount() == 0;
	}
	
	public void vectorClear() {
		while(this.vectorCount() != 0) {
			this.vectorPop();
		}
	}
	
	public T vectorBackOrNull() {
		// Wir können nicht zurückgehen oder greifen auf ein invalides Element zurück.
		// (Out of bounds).
		if (this.vectorInBoundsForAt(this.readIndex - 1) == false) {
			return null;
		}
		return this.vectorAt(this.readIndex - 1);
	}
	
	public T vectorBackPtrOrNull() {
		T ptr = this.vectorBackOrNull();
		if (ptr != null) {
			return ptr;
		}
		return null;
	}
	
	public T vectorBack() {
		this.vectorAssertBoundsForPop(this.readIndex - 1);
		return this.vectorAt(this.readIndex - 1);
	}
	
	public int vectorCount() {
		return this.count;
	}
}
