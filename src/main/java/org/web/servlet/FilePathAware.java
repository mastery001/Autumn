package org.web.servlet;

import java.util.Map;

/**
 * 向Action注入文件路径
 * @author mastery
 * @Time 2015-3-29 下午3:49:56
 * 
 */
public interface FilePathAware {
	
	void setFilePath(Map<String , String> filePath);
}
