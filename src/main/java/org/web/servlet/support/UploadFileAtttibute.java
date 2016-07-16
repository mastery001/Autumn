package org.web.servlet.support;

import tool.mastery.core.StringUtil;

public class UploadFileAtttibute {

	private static final String DEFAULT_SAVE_DIR = "upload";
	
	private static final String DEFAULT_TEMP_DIR = "temp";
	
	private static final UploadFileAtttibute DEFAULT = new UploadFileAtttibute();

	private int maxSize;

	private static CalendarHelper oldCh;

	private String tempDir;

	private String basePath;
	
	private String savePath;

	private boolean isModifyName = true;

	// 是否需要用日期再作为目录
	private boolean isDateDirectory = false;

	private UploadFileAtttibute() {
		super();
	}

	public static UploadFileAtttibute getInstance() {
		return DEFAULT;
	}

	public static UploadFileAtttibute updateUploadFileAtttibute() {
		if (DEFAULT.isDateDirectory) {
			CalendarHelper ch = new CalendarHelper();
			if (DEFAULT == null) {
				oldCh = ch;
				DEFAULT.setSavePath(DEFAULT.basePath + "/"
						+ oldCh.toFileFormat());
			} else {
				if (!ch.equals(oldCh)) {
					DEFAULT.setSavePath(DEFAULT.basePath + "/"
							+ ch.toFileFormat());
				}
			}
		}
		return DEFAULT;
	}

	private UploadFileAtttibute(int maxSize, String tempDir, String savePath) {
		super();
		this.maxSize = maxSize;
		this.tempDir = tempDir;
		this.savePath = savePath;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public String getTempDir() {
		if(StringUtil.StringIsNull(tempDir)) {
			return DEFAULT_TEMP_DIR;
		}
		return tempDir;
	}

	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}

	public String getSavePath() {
		if(StringUtil.StringIsNull(savePath)) {
			return DEFAULT_SAVE_DIR;
		}
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public boolean isModifyName() {
		return isModifyName;
	}

	public void setModifyName(boolean isModifyName) {
		this.isModifyName = isModifyName;
	}

	public boolean isDateDirectory() {
		return isDateDirectory;
	}

	public void setDateDirectory(boolean isDateDirectory) {
		this.isDateDirectory = isDateDirectory;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
		this.savePath = basePath;
	}

	@Override
	public String toString() {
		return "[maxSize=" + maxSize + ", tempDir="
				+ tempDir + ", savePath=" + savePath
				+ ", isModifyName=" + isModifyName + ", isDateDirectory="
				+ isDateDirectory + "]";
	}
	
}
