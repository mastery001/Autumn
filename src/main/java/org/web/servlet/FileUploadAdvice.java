package org.web.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

public interface FileUploadAdvice {
	
	public static final int MAX_SIZE = 3 * 1024 * 1024;

	/**
	 * 返回存储的文件全名
	 * 
	 * @param savePath
	 * @return
	 * @throws IOException
	 */
	Map<String, String> upload(String savePath, boolean isModifyName)
			throws IOException;

	Map<String, List<String>> getReqeustParams();

	int getMaxSize();

	Map<String, FileItem> getAllFiles();
}
