package compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PushbackInputStream;

public class CompileProcessInputFile {
	private File fp;
	private String absPath;
	private PushbackInputStream inStream;
	
	public CompileProcessInputFile(File fp, String absPath) throws FileNotFoundException {
		this.setFp(fp);
		this.setAbsPath(absPath);
		this.setInStream(new PushbackInputStream(new FileInputStream(fp), 2));
	}

	private void setInStream(PushbackInputStream inStream) {
		this.inStream = inStream;
		
	}

	public File getFp() {
		return fp;
	}

	public void setFp(File fp) {
		this.fp = fp;
	}

	public String getAbsPath() {
		return absPath;
	}

	public void setAbsPath(String absPath) {
		this.absPath = absPath;
	}

	public PushbackInputStream getInStream() {
		return inStream;
	}
}
