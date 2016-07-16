package org.web.framework.support;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.web.exception.BeanInitializationException;
import org.web.framework.ConfigLibrary;
import org.web.framework.ObjectRelationMap;

import tool.mastery.core.ClassUtil;
import tool.mastery.core.StringUtil;
import tool.mastery.dom.ReadXmlUtils;

public class XmlBeanFactory extends ObjectRelationMap {

	private static final String DEFAULT = "default";

	public XmlBeanFactory() throws Exception {
		super();
	}

	private static final Map<String, Object> beans = new HashMap<String, Object>();

	@Override
	public Object getBean(String name)  throws BeanInitializationException{
		// TODO Auto-generated method stub
		name = name.toLowerCase();
		if (!beans.containsKey(name)) {
			name = DEFAULT;
		}
		return beans.get(name);
	}

	@Override
	protected void configByProperties() throws IOException, Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.web.framework.ObjectRelationMap#configByXml()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void configByXml() throws DocumentException, Exception {
		try {
			Element rootElement = ReadXmlUtils
					.getRootElementByPath(ConfigLibrary.BEANSXMLPATH);
			List<Element> list = (List<Element>) rootElement.elements("bean");
			// 先设置默认的配置
			beans.put(DEFAULT, DefaultBeansConfig.getInstance());
			// 先扫描出id为default的配置,设置默认的配置完成
			for (int i = 0; i < list.size(); i++) {
				Element element = (Element) list.get(i);
				String id = element.attributeValue("id").toLowerCase();
				if (isDefaultNode(id)) {
					assignmentProperty(element, id);
					break;
				}
			}
			for (int i = 0; i < list.size(); i++) {
				Element element = (Element) list.get(i);
				String id = element.attributeValue("id").toLowerCase();
				if (StringUtil.StringIsNull(id) || isDefaultNode(id)) {
					continue;
				}
				Object newConfig = ((BeansConfig) beans.get(DEFAULT)).clone();
				beans.put(id, newConfig);
				assignmentProperty(element, id);
			}
		} catch (DocumentException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanInitializationException(
					"配置的class属性当是service时必须实现OperateService类，当是dao时必须实现OperateDao类");
		}
	}

	/**
	 * 给属性赋值
	 * @param element
	 * @param id
	 * @throws Exception
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	private void assignmentProperty(Element element, String id)
			throws Exception, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		List<Element> propertyElements = (List<Element>) element
				.elements("property");
		for (Element propertyElement : propertyElements) {
			String name = propertyElement.attributeValue("name");
			String clazz = propertyElement.attributeValue("class");
			if(StringUtil.StringIsNull(clazz)) {
				continue;
			}
			Object config = beans.get(id);
			//System.out.println(config);
			String methodName = name.substring(0, 1).toUpperCase()
					+ name.substring(1, name.length());
			assignment(config, methodName, clazz);
		}
	}

	private boolean isDefaultNode(String id) {
		return DEFAULT.equalsIgnoreCase(id);
	}

	/**
	 * 赋值操作
	 * 
	 * @param config
	 * @param name
	 * @param propertyClass
	 * @throws Exception
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void assignment(Object config, String name,
			String propertyClass) throws Exception, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		Object obj = ClassUtil.getObjectByName(propertyClass);
		String methodName = name.substring(0, 1).toUpperCase()
				+ name.substring(1, name.length());
		Method setMethod = this.getMethod(config.getClass(), obj.getClass(),
				"set" + methodName);
		setMethod.invoke(config, obj);
	}

	private Method getMethod(Class<?> objClass, Class<?> paramClass,
			String methodName) throws SecurityException, NoSuchMethodException {
		if (paramClass.getInterfaces().length == 0) {
			return this.getMethod(objClass, paramClass.getSuperclass(),
					methodName);
		}
		return objClass.getMethod(methodName, paramClass.getInterfaces()[0]);
	}

}
