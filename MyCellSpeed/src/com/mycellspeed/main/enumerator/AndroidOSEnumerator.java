package com.mycellspeed.main.enumerator;

public class AndroidOSEnumerator {
	private enum AndroidOSEnum {
		BASE (android.os.Build.VERSION_CODES.BASE, "Base")
		,BASE_1_1 (android.os.Build.VERSION_CODES.BASE_1_1, "Base 1.1")
		,CUPCAKE (android.os.Build.VERSION_CODES.CUPCAKE, "Cupcake")
		,DONUT (android.os.Build.VERSION_CODES.DONUT, "Donut")
		,ECLAIR (android.os.Build.VERSION_CODES.ECLAIR, "Eclair")
		,ECLAIR_0_1 (android.os.Build.VERSION_CODES.ECLAIR_0_1, "Eclair 0.1")
		,ECLAIR_MR1 (android.os.Build.VERSION_CODES.ECLAIR_MR1, "Eclair MR-1")
		,FROYO (android.os.Build.VERSION_CODES.FROYO, "Froyo")
		,GINGERBREAD (android.os.Build.VERSION_CODES.GINGERBREAD, "Gingerbread")
		,GIGNERBREAD_MR1 (android.os.Build.VERSION_CODES.GINGERBREAD_MR1, "Gingerbread MR-1")
		,HONEYCOMB (android.os.Build.VERSION_CODES.HONEYCOMB, "Honeycomb")
		,HONEYCOMB_MR1 (android.os.Build.VERSION_CODES.HONEYCOMB_MR1, "Honeycomb MR-1")
		,HONEYCOMB_MR2 (android.os.Build.VERSION_CODES.HONEYCOMB_MR2, "Honeycomb MR-2")
		,ICE_CREAM_SANDWICH (android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH, "Ice Cream Sandwich")
		,ICE_CREAM_SANDWICH_MR1 (android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1, "Ice Cream Sandwich MR-1")
		,JELLY_BEAN (android.os.Build.VERSION_CODES.JELLY_BEAN, "Jelly Bean")
		,JELLY_BEAN_MR1 (android.os.Build.VERSION_CODES.JELLY_BEAN_MR1, "Jelly Bean MR-1")
		,JELLY_BEAN_MR2 (android.os.Build.VERSION_CODES.JELLY_BEAN_MR2, "Jelly Bean MR-2")
		//,KITKAT (android.os.Build.VERSION_CODES.KITKAT, "Kit Kat")
		;
		
		private final int key;
		private final String value;
		
		AndroidOSEnum(int k, String v) {
			key = k;
			value = v;
		}
		
		private int key() { return key; }
		private String value() { return value; }
	}
	
	public static String getValue(int key) {
		for (AndroidOSEnum e : AndroidOSEnum.values())
			if (e.key() == key) return e.value();
		return "NaN";
	}
}
