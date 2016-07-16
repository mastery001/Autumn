package org.web.framework.action.support;

import org.web.exception.AutumnException;
import org.web.framework.ConfigLibrary;
import org.web.framework.action.config.ConstantConfig;
import org.web.servlet.support.UploadFileAtttibute;

import tool.mastery.core.StringUtil;
import tool.mastery.log.LogUtil;

/**
 * 注入常量的操作
 * 
 * @author mastery
 * @Time 2015-4-17 下午1:31:36
 * 
 */
public class InjectAttribute {

	public void init() {
		injectUploadFileAttribute();
	}

	private void injectUploadFileAttribute() {
		UploadFileAtttibute ufa = UploadFileAtttibute.getInstance();

		// 设置是否需要修改文件名
		String isModifyName = getValue(ConfigLibrary.MULTIPARY_MODIFY,
				Boolean.class.getSimpleName());
		if (isModifyName != null) {
			ufa.setModifyName(Boolean.parseBoolean(isModifyName));
		}

		String isDateDirectory = getValue(
				ConfigLibrary.MULTIPARY_ISDATEDIRECTORY,
				Boolean.class.getSimpleName());
		if (isDateDirectory != null) {
			ufa.setDateDirectory(Boolean.parseBoolean(isDateDirectory));
		}

		String maxSize = getValue(ConfigLibrary.MULTIPARY_MAXSIZE,
				Integer.class.getSimpleName());
		if (maxSize != null) {
			ufa.setMaxSize(Integer.parseInt(maxSize));
		}

		String tempDir = getValue(ConfigLibrary.MULTIPARY_TEMP, null);
		if (tempDir != null) {
			ufa.setTempDir(tempDir);
		}

		String basePath = getValue(ConfigLibrary.MULTIPARY_SAVEDIR, null);
		if (basePath != null) {
			ufa.setBasePath(basePath);
		}
		LogUtil.getLogger().debug(
				"the multipart attribute is : " + ufa);
	}

	private String getValue(String name, String className) {
		// 如果该值不存在
		if (!ConstantConfig.getInstance().constains(name)) {
			return null;
		}
		if (className == null) {
			className = "String";
		}
		String value = ConstantConfig.getInstance().get(name);
		if (StringUtil.StringIsNull(value)) {
			throw new AutumnException("属性名为" + name + "的属性配置必须是" + className
					+ "类型的数值");
		}
		return value;
	}
}
