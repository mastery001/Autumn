package org.web.framework;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;
import org.web.framework.service.BeanFactory;

/**
 * 对象映射模型类
 * @author mastery
 * @Time 2015-3-16 下午5:26:24
 * 
 */
public abstract class ObjectRelationMap implements BeanFactory {

	protected Map<String, Object> orm = new HashMap<String, Object>();

	public ObjectRelationMap() throws Exception {
		try {
			// 先看看xml是否配置，若配置好则读取xml，若未配置好则直接读取properties
			configByXml();
		} catch (DocumentException e) {
			configByProperties();
		}
	}

	protected abstract void configByProperties() throws IOException, Exception;

	protected abstract void configByXml() throws DocumentException, Exception;

	protected String processReg(Object key) {
		return key.toString().replace("*", ".+");
	}
}
