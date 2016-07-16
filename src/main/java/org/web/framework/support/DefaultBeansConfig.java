package org.web.framework.support;

import org.web.dao.core.support.DefaultDaoAdvice;
import org.web.service.DefaultOperateService;

public class DefaultBeansConfig extends BeansConfig{
	
	public static DefaultBeansConfig getInstance(){
		return new DefaultBeansConfig();
	}
	
	private DefaultBeansConfig() {
		this.setName("default");
		this.setService(new DefaultOperateService());
		this.setDao(new DefaultDaoAdvice());
	}
}
