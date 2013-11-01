package com.mycellspeed.main.singleton;

import java.util.ArrayList;

import com.mycellspeed.main.data.DataUnit;

public class DataUnitHandler {
	
	private static DataUnitHandler instance;
	
	private ArrayList<DataUnit> data = new ArrayList<DataUnit>();
	
	private DataUnitHandler() {}
	
	public static synchronized DataUnitHandler getInstance() {
		if (instance == null)
			instance = new DataUnitHandler();
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		/*preventing the singleton object from being cloned*/
		throw new CloneNotSupportedException();
	}
	
	public boolean addDataUnit(DataUnit unit, boolean overwrite) {
		if (!findDataUnit(unit.getKey())) return data.add(unit);
		else {
			if (overwrite) {
				while (removeDataUnit(unit.getKey())) ;
				return data.add(unit);
			} else return false;
		}
	}
	
	public boolean addDataUnit(String key, String value, String type, boolean overwrite) {
		return addDataUnit(new DataUnit(key, value, type), overwrite);
	}
	
	public boolean findDataUnit(String key) {
		for (DataUnit unit : data) if (unit.getKey().equals(key)) return true;
		return false;
	}
	
	public String pullDataUnit(String key) {
		for (DataUnit unit : data) if (unit.getKey().equals(key)) return unit.getValue();
		return "NaN";
	}
	
	public boolean removeDataUnit(String key) {
		for (DataUnit unit : data) if (unit.getKey().equals(key)) return data.remove(unit);
		return false;
	}
	
	public ArrayList<DataUnit> retrieveData() { return data; }
	
	public void clearHandler() { data.clear(); }
}
