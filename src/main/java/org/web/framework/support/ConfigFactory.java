package org.web.framework.support;

import org.web.exception.BeanInitializationException;
import org.web.framework.Configuration;
import org.web.framework.service.BeanFactory;

/**
 * dao和service的工厂类
 * 
 * @author mastery
 * @Time 2015-4-1 下午1:49:05
 * 
 */
public class ConfigFactory implements BeanFactory {

	private EnumBean enumBean;

	public ConfigFactory(EnumBean enumBean) {
		this.enumBean = enumBean;
	}

	public EnumBean getEnumBean() {
		return enumBean;
	}

	public void setEnumBean(EnumBean enumBean) {
		this.enumBean = enumBean;
	}

	@Override
	public Object getBean(String name) throws BeanInitializationException {
		if (name == null) {
			throw new BeanInitializationException("name参数不能为空");
		}
		BeansConfig config = null;
		try {
			config = (BeansConfig) Configuration.getBeanFactory().getBean(
					name);
		} catch (Exception e) {
			throw new BeanInitializationException(e);
		}
		config.setName(name);
		return enumBean.get(config);
	}
	
	public Object getVoProcessor(String name) {
		try {
			BeansConfig config = (BeansConfig) Configuration.getBeanFactory().getBean(
					name);
			return enumBean.get(config);
		} catch (Exception e) {
			// 按理说该异常不会抛出
			e.printStackTrace();
		}
		return null;
	}
}
