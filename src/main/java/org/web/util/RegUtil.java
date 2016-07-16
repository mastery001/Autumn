package org.web.util;

public class RegUtil {
	public static String processReg(Object key) {
		return key.toString().replace("*", ".+");
	}
	
	/**
	 * 去除花括号
	 * @param str
	 * @return
	 */
	public static String deleteBraces(String str) {
		String newStr = "";
		char[] chs = str.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			String s = String.valueOf(chs[i]);
			if(!isBraces(s)) {
				newStr += s;
			}
		}
		return newStr;
	}
	
	/**
	 * 判断是否含有花括号
	 * @param str
	 * @return
	 */
	public static boolean isBraces(String str) {
		if(str.matches("[{}]+")) {
			return true;
		}
		return false;
	}
}
