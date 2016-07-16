package org.web.access.factory;


import org.web.dao.core.DaoAdvice;
import org.web.exception.BeanInitializationException;
import org.web.framework.service.BeanFactory;
import org.web.framework.support.ConfigFactory;
import org.web.framework.support.EnumBean;

/**
 * dao工厂，用于获取dao层的服务
 * 
 * @author mastery
 * @Time 2015-3-16 下午8:32:05
 * 
 */
public class DaoAdviceFactory {

	public static DaoAdvice getDao(String name)
			throws BeanInitializationException {
		BeanFactory bf = new ConfigFactory(EnumBean.Dao);
		return (DaoAdvice)bf.getBean(name);
	}
}
