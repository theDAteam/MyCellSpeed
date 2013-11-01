package com.mycellspeed.main.data;

public class DataUnit {
	
	private String key;
	private String value;
	private String type;
	
	public DataUnit(String k, String v, String t) {
		key = k;
		value = v;
		type = t;
	}
	
	public String getKey() { return key; }
	public String getValue() { return value; }
	public String getType() { return type; }

}
