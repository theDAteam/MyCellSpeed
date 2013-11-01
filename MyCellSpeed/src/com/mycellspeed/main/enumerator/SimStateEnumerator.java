package com.mycellspeed.main.enumerator;

import android.telephony.TelephonyManager;

public class SimStateEnumerator {
	private enum SimStateEnum {
		UNKNOWN (TelephonyManager.SIM_STATE_UNKNOWN, "Unknown")
		,ABSENT (TelephonyManager.SIM_STATE_ABSENT, "Absent")
		,CDMA (TelephonyManager.SIM_STATE_PIN_REQUIRED, "PIN Required")
		,PUK (TelephonyManager.SIM_STATE_PUK_REQUIRED, "PUK Required")
		,NETWORK (TelephonyManager.SIM_STATE_NETWORK_LOCKED, "Network Locked")
		,READY (TelephonyManager.SIM_STATE_READY, "Ready")
		;
		
		private final int key;
		private final String value;
		
		SimStateEnum(int k, String v) {
			key = k;
			value = v;
		}
		
		private int key() { return key; }
		private String value() { return value; }
	}
	
	public static String getValue(int key) {
		for (SimStateEnum e : SimStateEnum.values())
			if (e.key() == key) return e.value();
		return "NaN";
	}
}
