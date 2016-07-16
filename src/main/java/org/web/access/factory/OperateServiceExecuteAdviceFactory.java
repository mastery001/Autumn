package org.web.access.factory;

import org.web.exception.BeanInitializationException;
import org.web.framework.service.BeanFactory;
import org.web.framework.support.ConfigFactory;
import org.web.framework.support.EnumBean;
import org.web.service.OperateServiceExecuteAdvice;

/**
 * service工厂，用于获取操作service的服务
 * 
 * @author mastery
 * @Time 2015-3-16 下午8:32:57
 * 
 */
public class OperateServiceExecuteAdviceFactory {

	public static OperateServiceExecuteAdvice getService(String name)
			throws BeanInitializationException {
		BeanFactory bf = new ConfigFactory(EnumBean.Service);
		return (OperateServiceExecuteAdvice) bf.getBean(name);
	}
}
