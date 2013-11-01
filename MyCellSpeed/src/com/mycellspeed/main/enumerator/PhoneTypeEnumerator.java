package com.mycellspeed.main.enumerator;

import android.telephony.TelephonyManager;

public class PhoneTypeEnumerator {
	private enum PhoneTypeEnum {
		NONE (TelephonyManager.PHONE_TYPE_NONE, "None")
		,GSM (TelephonyManager.PHONE_TYPE_GSM, "GSM")
		,CDMA (TelephonyManager.PHONE_TYPE_CDMA, "CDMA")
		,SIP (TelephonyManager.PHONE_TYPE_SIP, "SIP")
		;
		
		private final int key;
		private final String value;
		
		PhoneTypeEnum(int k, String v) {
			key = k;
			value = v;
		}
		
		private int key() { return key; }
		private String value() { return value; }
	}
	
	public static String getValue(int key) {
		for (PhoneTypeEnum e : PhoneTypeEnum.values())
			if (e.key() == key) return e.value();
		return "NaN";
	}
}
