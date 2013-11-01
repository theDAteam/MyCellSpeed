package com.mycellspeed.main.singleton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DataFileHandler {
	
	private static DataFileHandler instance;
	
	private Boolean fileLock;
	private Boolean isOpen;
	private File dataFile;
	
	private DataFileHandler() {
		fileLock = false;
		isOpen = false;
	}
	
	public static synchronized DataFileHandler getInstance() {
		if (instance == null)
			instance = new DataFileHandler();
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		/*preventing the singleton object from being cloned*/
		throw new CloneNotSupportedException();
	}
	
	// handling file lock contention
	public boolean isFileLocked() { return fileLock; }
	public void lockFile() { fileLock = true; }
	public void unlockFile() { fileLock = false; }
	
	// handling file availability
	public boolean isFileOpen() { return isOpen; }
	public void openFile(File newFile) {
		dataFile = newFile;
		dataFile.setWritable(true);
		dataFile.setReadable(false);
		isOpen = true;
	}
	public void closeFile() {
		dataFile.setWritable(false);
		dataFile.setReadable(true);
		isOpen = false;
	}
	
	public boolean writeLineToFile(String line) throws FileNotFoundException, Exception, Throwable {
		// write specified string to specified filename, local to app
		if (!fileLock && isOpen) {
			lockFile();
			PrintWriter pw = null;
			pw = new PrintWriter(dataFile);
			if (dataFile.length() > 0)
				pw.println();
			pw.append(line);
			pw.flush();
			pw.close();
			unlockFile();
			return true;
		}
		return false;
	}
}
