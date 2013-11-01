package com.mycellspeed.main.util;

import java.util.Locale;

public final class StringUtil {
	
	public static String capitalizeFirst(String i, Locale l) {
		return i.substring (0,1).toUpperCase(l) + i.substring(1).toLowerCase(l);
	}

}
