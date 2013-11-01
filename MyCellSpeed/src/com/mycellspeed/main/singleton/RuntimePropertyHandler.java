package com.mycellspeed.main.singleton;

public class RuntimePropertyHandler {
	
	private static RuntimePropertyHandler instance;
	
	private static Boolean loadSuccess;
	
	private RuntimePropertyHandler() {}
	
	public static synchronized RuntimePropertyHandler getInstance() {
		if (instance == null)
			instance = new RuntimePropertyHandler();
		loadProperties();
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		/*preventing the singleton object from being cloned*/
		throw new CloneNotSupportedException();
	}
	
	private static void loadProperties() {
		try {
			//TODO load properties from database
			
			loadSuccess = Boolean.TRUE;
		} catch (Throwable e) {
			loadSuccess = Boolean.FALSE;
		}
	}
	
	public Boolean isLoadSuccess() { return loadSuccess; }
	
	
}
