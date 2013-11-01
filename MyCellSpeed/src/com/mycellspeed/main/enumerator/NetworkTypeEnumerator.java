package com.mycellspeed.main.enumerator;

import android.telephony.TelephonyManager;

public final class NetworkTypeEnumerator {
	private enum NetworkTypeEnum {
		UNKNOWN (TelephonyManager.NETWORK_TYPE_UNKNOWN, "Unknown")
		,GPRS (TelephonyManager.NETWORK_TYPE_GPRS, "GPRS")
		,EDGE (TelephonyManager.NETWORK_TYPE_EDGE, "EDGE")
		,UMTS (TelephonyManager.NETWORK_TYPE_UMTS, "UMTS")
		,CDMA (TelephonyManager.NETWORK_TYPE_CDMA, "CDMA")
		,EVDO_0 (TelephonyManager.NETWORK_TYPE_EVDO_0, "EVDO-0")
		,EVDO_A (TelephonyManager.NETWORK_TYPE_EVDO_A, "EVDO-A")
		,RTT (TelephonyManager.NETWORK_TYPE_1xRTT, "1vRTT")
		,HSDPA (TelephonyManager.NETWORK_TYPE_HSDPA, "HSDPA")
		,HSUPA (TelephonyManager.NETWORK_TYPE_HSUPA, "HSUPA")
		,HSPA (TelephonyManager.NETWORK_TYPE_HSPA, "HSPA")
		,IDEN (TelephonyManager.NETWORK_TYPE_IDEN, "IDEN")
		,EVDO_B (TelephonyManager.NETWORK_TYPE_EVDO_B, "EVDO-B")
		,LTE (TelephonyManager.NETWORK_TYPE_LTE, "LTE")
		,EHRPD (TelephonyManager.NETWORK_TYPE_EHRPD, "EHRPD")
		,HSPAP (TelephonyManager.NETWORK_TYPE_HSPAP, "HSPAP")
		;
		
		private final int key;
		private final String value;
		
		NetworkTypeEnum(int k, String v) {
			key = k;
			value = v;
		}
		
		private int key() { return key; }
		private String value() { return value; }
	}
	
	public static String getValue(int key) {
		for (NetworkTypeEnum e : NetworkTypeEnum.values())
			if (e.key() == key) return e.value();
		return "NaN";
	}

}
