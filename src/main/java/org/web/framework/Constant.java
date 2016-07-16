package org.web.framework;

public class Constant {

	public static final String ADD = "add";

	public static final String UPDATE = "update";

	public static final String DELETE = "delete";

	public static final String IMPORT = "import";

	public static final String EXPORT = "export";

	public static final String[] WORD_FORMAT = new String[] { "doc", "docx",
			"docm", "dotx", "dotm" };

	public static final String[] EXCEL_FORMAT = new String[] { "xls", "xlsx",
			"xlsm", "xltx", "xltm", "xlsb", "xlam" };

	public static final String[] IMAGE_FORMAT = new String[] { "bmp", "gif", "jpg", "png",
			"jpeg", "ico"};

	public static final String WORD = "word";
	public static final String EXCEL = "excel";
	public static final String IMAGE = "image";
	public static final String OTHER = "other";
	
	/**
	 * 将操作名转换成中文
	 * 
	 * @param operate
	 * @return
	 */
	public static String changeOperateToChinese(String operate) {
		if (operate != null) {
			if (operate.equalsIgnoreCase(Constant.ADD)
					|| operate.equalsIgnoreCase(Constant.IMPORT)) {
				return "添加";
			} else if (operate.equalsIgnoreCase(Constant.UPDATE)) {
				return "修改";
			} else {
				return "删除";
			}
		}
		return null;
	}
}
