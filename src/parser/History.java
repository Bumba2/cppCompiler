package parser;

// Wir wollen commands heruntercasten an rekursive Funtkionen.
public class History<T> {
	private int flags;
	private ParserHistorySwitch<T> _switch; // Tracked den Status der geparseden Cases im Switch-Statement.
	
	public History() {}
	
	public History(int flags) {
		this.flags = flags;
		this.set_switch(null);
	}
	
	public History(int flags, ParserHistorySwitch<T> _switch) {
		this.flags = flags;
		this.set_switch(_switch);
	}
	
	public int getFlags() {
		return this.flags;
	}
	
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	public void memcpy(History<T> history) {
		this.flags = history.getFlags();
		this._switch = history._switch;
	}
	
	public ParserHistorySwitch<T> get_switch() {
		return _switch;
	}

	public void set_switch(ParserHistorySwitch<T> _switch) {
		this._switch = _switch;
	}
	
	
}
