package org.web.framework;

public class ConfigLibrary {
	
	public static final String BEANSXMLPATH = "beans.xml";
	
	public static final String ACTIONXMLPATH = "action.xml";
	
	public static final String ACTIONPROPERTIESPATH = "action.properties";
	
	public static final String DBPROPERTIESPATH = "db.properties";
	
	public static final String CLASSPROPERTIESPATH = "class.properties";
	
	// 默认是服务器端跳转
	public static final String DEFAULT_RESULT_TYPE = "dispatcher";

	// 文件是否允许修改，名字
	public static final String MULTIPARY_MODIFY = "multipart.modifyFileName";
	
	// 文件的最大值
	public static final String MULTIPARY_MAXSIZE = "multipart.maxsize";
	
	// 存放文件的保存路径
	public static final String MULTIPARY_SAVEDIR = "multipart.saveDir";
	
	// 存放文件的临时路径
	public static final String MULTIPARY_TEMP = "multipart.temp";
	
	// 在文件的保存路径后添加日期作为后续目录
	public static final String MULTIPARY_ISDATEDIRECTORY = "multipart.isDateDirectory";
	
	public static final String MULTIPART_FILEDATA = "MULTIPART_FILEDATA";
}
