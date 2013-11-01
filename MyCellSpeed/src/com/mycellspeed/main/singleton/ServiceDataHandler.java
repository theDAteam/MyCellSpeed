package com.mycellspeed.main.singleton;

import android.util.Log;

public class ServiceDataHandler {
	private static ServiceDataHandler instance;
	
	private String imsiID = null;
	private String phoneNumber = null;
	private String groupIDLevelOne = null;
	
	private String networkType = null;
	private String networkCountryISO = null;
	private String networkOperatorName = null;
	private String mcc = null;
	private String mnc = null;
	
	private Integer simStateValue;
	private String simStateFull = null;
	private String simSerialNumber = null;
	private String simCountryISO = null;
	private String simOperatorName = null;
	private String simOperator = null;
	
	private ServiceDataHandler() {}
	
	public static synchronized ServiceDataHandler getInstance() {
		if (instance == null)
			instance = new ServiceDataHandler();
		return instance;
	}
	
	public Object clone() throws CloneNotSupportedException {
		/*preventing the singleton object from being cloned*/
		throw new CloneNotSupportedException();
	}
	
	public String getImsiID() { return imsiID; }
	public void setImsiID(String value) { imsiID = value; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String value) { phoneNumber = value; }
	public String getGroupIDLevelOne() { return groupIDLevelOne; }
	public void setGroupIDLevelOne(String value) { groupIDLevelOne = value; }
	
	public String getNetworkType() { return networkType; }
	public void setNetworkType(String value) { networkType = value; }
	public String getNetworkCountryISO() { return networkCountryISO; }
	public void setNetworkCountryISO(String value) { networkCountryISO = value; }
	public String getNetworkOperatorName() { return networkOperatorName; }
	public void setNetworkOperatorName(String value) { networkOperatorName = value; }
	public String getMobileCountryCode() { return mcc; }
	public void setMobileCountryCode(String value) { mcc = value; }
	public String getMobileNetworkCode() { return mnc; }
	public void setMobileNetworkCode(String value) { mnc = value; }
	
	public Integer getSimStateValue() { return simStateValue; }
	public void setSimStateValue(Integer value) { simStateValue = value; }
	public String getSimStateFull() { return simStateFull; }
	public void setSimStateFull(String value) { simStateFull = value; }
	public String getSimSerialNumber() { return simSerialNumber; }
	public void setSimSerialNumber(String value) { simSerialNumber = value; }
	public String getSimCountryISO() { return simCountryISO; }
	public void setSimCountryISO(String value) { simCountryISO = value; }
	public String getSimOperatorName() { return simOperatorName; }
	public void setSimOperatorName(String value) { simOperatorName = value; }
	public String getSimOperator() { return simOperator; }
	public void setSimOperator(String value) { simOperator = value; }
	
	public void loadDataPoint() {
		boolean srvDataLoaded = true;
		
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("IMSI", imsiID, "N", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("PhnNbr", phoneNumber, "N", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("NetOper", mcc+mnc, "S", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("SimOper", simOperator, "S", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("SimSrlNbr", simSerialNumber, "N", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("SimState", "" + simStateValue, "N", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("SimCtryCode", simCountryISO, "S", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("NetType", networkType, "S", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("MNC", mnc, "S", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("MCC", mcc, "S", true));
		else srvDataLoaded = false;
		if (srvDataLoaded && DataUnitHandler.getInstance().addDataUnit("GrpId", groupIDLevelOne, "S", true));
		else srvDataLoaded = false;
		
		if (!srvDataLoaded) {
			Log.e("ServiceDataHandler", "Failed loading phone service data, clearing & forcing retry.");
			while (DataUnitHandler.getInstance().removeDataUnit("IMSI"));
			while (DataUnitHandler.getInstance().removeDataUnit("PhnNbr"));
			while (DataUnitHandler.getInstance().removeDataUnit("NetOper"));
			while (DataUnitHandler.getInstance().removeDataUnit("SimOper"));
			while (DataUnitHandler.getInstance().removeDataUnit("SimSrlNbr"));
			while (DataUnitHandler.getInstance().removeDataUnit("SimState"));
			while (DataUnitHandler.getInstance().removeDataUnit("SimCtryCode"));
			while (DataUnitHandler.getInstance().removeDataUnit("NetType"));
			while (DataUnitHandler.getInstance().removeDataUnit("MNC"));
			while (DataUnitHandler.getInstance().removeDataUnit("MCC"));
			while (DataUnitHandler.getInstance().removeDataUnit("GrpId"));
			loadDataPoint();
		}
	}
}
