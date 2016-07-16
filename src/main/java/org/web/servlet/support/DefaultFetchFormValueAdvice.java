package org.web.servlet.support;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.web.exception.ActionExecuteException;
import org.web.framework.Constant;
import org.web.servlet.FetchFormValueAdvice;

import tool.mastery.core.BeanUtil;
import tool.mastery.core.ClassUtil;

public class DefaultFetchFormValueAdvice implements FetchFormValueAdvice {

	private HttpServletRequest request;

	private String name;

	public DefaultFetchFormValueAdvice(HttpServletRequest request, String name) {
		this.request = request;
		this.name = name;
	}

	@Override
	public Object fetchFormObjectValue() throws ActionExecuteException {
		// 获得对应的bean对象
		try {
			List<Object> list = createBeanFromParam(name);
			if (list != null) {
				return list.get(0);
			}
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		}
		return null;
	}

	@Override
	public List<Object> fetchFormListValue() throws ActionExecuteException {
		List<Object> list = null;
		// 获得对应的bean对象
		try {
			list = createBeanFromParam(name);
		} catch (Exception e) {
			throw new ActionExecuteException(e);
		}
		return list;
	}

	/**
	 * 从前台接收的表单后生成对应的bean对象
	 * 
	 * @param request
	 * @param name
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	private List<Object> createBeanFromParam(String name)
			throws ActionExecuteException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		List<Object> objList = null;
		Object bean = ClassUtil.getBeanByClassName(name);
		Enumeration<?> enu = request.getParameterNames();
		// 判断是否赋值
		boolean flag = false;
		while (enu.hasMoreElements()) {
			String paramName = (String) enu.nextElement();
			String[] beanValues = request.getParameterValues(paramName
					.toString());
			if (beanValues.length == 1) {
				String value = request.getParameter(paramName).trim();
				try {
					BeanUtil.setProperty(bean, paramName, value);
					flag = true;
				} catch (NoSuchMethodException e) {
					continue;
				}
			} else {
				try {
					// 添充Bean值
					PropertyUtils.setProperty(bean, paramName.toString(),
							beanValues);
					flag = true;
				} catch (IllegalArgumentException e) {
					Object executeOperateGrant = request
							.getAttribute("executeOperateGrant");
					// 如果不是删除操作则报错！
					if (executeOperateGrant != null
							&& !(executeOperateGrant.toString()
									.equalsIgnoreCase(Constant.DELETE) || executeOperateGrant
									.toString().equalsIgnoreCase(
											Constant.EXPORT))) {
						throw new ActionExecuteException(e.getMessage());
					}
					objList = new ArrayList<Object>();
					// 如果此处遇到IllegalArgumentException异常则就是得到的值为1个对象的多个主键值
					for (int i = 0; i < beanValues.length; i++) {
						Object obj = ClassUtil.getBeanByClassName(name);
						try {
							BeanUtil.setProperty(obj, paramName, beanValues[i]);
						} catch (NoSuchMethodException e1) {
							continue;
						}
						objList.add(obj);
					}
					flag = true;
					break;
				}
			}
		}
		if (!flag) {
			bean = null;
		} else {
			if (objList == null) {
				objList = new ArrayList<Object>();
				objList.add(bean);
			}
		}

		return objList;
	}

}
