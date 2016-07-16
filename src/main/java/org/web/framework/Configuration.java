package org.web.framework;

import org.web.framework.service.BeanFactory;
import org.web.framework.service.CacheMaster;
import org.web.framework.support.DefaultCacheMaster;
import org.web.framework.support.XmlBeanFactory;

public class Configuration {

	private static BeanFactory beanFactory;
	
	private static CacheMaster cacheMaster;

	public static BeanFactory getBeanFactory() throws Exception {
		if(beanFactory == null) {
			beanFactory = new XmlBeanFactory();
		}
		return beanFactory;
	}

	public static void setBeanFactory(BeanFactory beanFactory) {
		Configuration.beanFactory = beanFactory;
	}

	public static CacheMaster getCacheMaster() {
		if(cacheMaster == null) {
			cacheMaster = new DefaultCacheMaster();
		}
		return cacheMaster;
	}

	public static void setCacheMaster(CacheMaster cacheMaster) {
		Configuration.cacheMaster = cacheMaster;
	}
	
}	
