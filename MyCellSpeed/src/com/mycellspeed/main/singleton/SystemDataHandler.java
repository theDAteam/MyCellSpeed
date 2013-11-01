package com.mycellspeed.main.singleton;

import android.util.Log;

public class SystemDataHandler {
	private static SystemDataHandler instance;
	
	private String manufacturer;
	private String brand;
	private String device;
	private String model;
	private String phoneType;
	private String imei;
	private String deviceSoftwareVersion;
	private String androidOSName;
	private String androidOSVersion;
	
	private SystemDataHandler() {}
	
	public static synchronized SystemDataHandler getInstance() {
		if (instance == null)
			instance = new SystemDataHandler();
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		/*preventing the singleton object from being cloned*/
		throw new CloneNotSupportedException();
	}
	
	public String getManufacturer() { return manufacturer; }
	public void setManufacturer(String value) { manufacturer = value; }
	public String getBrand() { return brand; }
	public void setBrand(String value) { brand = value; }
	public String getDevice() { return device; }
	public void setDevice(String value) { device = value; }
	public String getModel() { return model; }
	public void setModel(String value) { model = value; }
	public String getPhoneType() { return phoneType; }
	public void setPhoneType(String value) { phoneType = value; }
	public String getIMEI() { return imei; }
	public void setIMEI(String value) { imei = value; }
	public String getDeviceSoftwareVersion() { return deviceSoftwareVersion; }
	public void setDeviceSoftwareVersion(String value) { deviceSoftwareVersion = value; }
	public String getAndroidOSName() { return androidOSName; }
	public void setAndroidOSName(String value) { androidOSName = value; }
	public String getAndroidOSVersion() { return androidOSVersion; }
	public void setAndroidOSVersion(String value) { androidOSVersion = value; }
	
	public void loadDataPoint(){
		boolean sysDataLoaded = true;
		
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("Mnfctr", manufacturer, "S", true));
		else sysDataLoaded = false;
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("Brand", brand, "S", true));
		else sysDataLoaded = false;
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("Device", device, "S", true));
		else sysDataLoaded = false;
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("Model", model, "S", true));
		else sysDataLoaded = false;
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("PhnType", phoneType, "S", true));
		else sysDataLoaded = false;
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("IMEI", imei, "S", true));
		else sysDataLoaded = false;
		if (sysDataLoaded && DataUnitHandler.getInstance().addDataUnit("OS", androidOSName + " - V " + androidOSVersion, "S", true));
		else sysDataLoaded = false;
		
		if (!sysDataLoaded) {
			Log.e("SystemDataHandler", "Failed loading system data, clearing & forcing retry.");
			while (DataUnitHandler.getInstance().removeDataUnit("Mnfctr"));
			while (DataUnitHandler.getInstance().removeDataUnit("Brand"));
			while (DataUnitHandler.getInstance().removeDataUnit("Device"));
			while (DataUnitHandler.getInstance().removeDataUnit("Model"));
			while (DataUnitHandler.getInstance().removeDataUnit("PhnType"));
			while (DataUnitHandler.getInstance().removeDataUnit("IMEI"));
			while (DataUnitHandler.getInstance().removeDataUnit("OS"));
			loadDataPoint();
		}
	}
}
