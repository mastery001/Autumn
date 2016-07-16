package org.web.framework.service;

import org.web.exception.BeanInitializationException;



public interface BeanFactory {
	
	Object getBean(String name) throws BeanInitializationException;
}
