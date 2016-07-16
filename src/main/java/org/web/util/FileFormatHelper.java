package org.web.util;

import org.web.framework.Constant;

/**
 * @ClassName: FileFormatHelper
 * 
 * @Description: 判断文件对应是何种格式
 * 
 * @author A18ccms a18ccms_gmail_com
 * 
 * @date 2015-6-22 上午10:43:32
 * 
 * 
 */

public class FileFormatHelper {

	public static String getFormat(String fileExt) {
		if (fileExt == null) {
			throw new NullPointerException();
		}
		// 判断是否是word形式
		for (String format : Constant.WORD_FORMAT) {
			if (fileExt.equalsIgnoreCase(format)) {
				return Constant.WORD;
			}
		}
		// 判断是否是excel形式
		for (String format : Constant.EXCEL_FORMAT) {
			if (fileExt.equalsIgnoreCase(format)) {
				return Constant.EXCEL;
			}
		}
		// 判断是否是image形式
		for (String format : Constant.IMAGE_FORMAT) {
			if (fileExt.equalsIgnoreCase(format)) {
				return Constant.IMAGE;
			}
		}
		return Constant.OTHER;
	}

}
