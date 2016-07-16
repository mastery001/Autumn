package org.web.service;



/**
 * 用于获得vo对象的处理类
 * @see org.web.service.VoProcessor
 * @author mastery
 * @Time 2015-3-8 下午11:15:58
 * 
 */
public interface VoProcessorAware {
	
	VoProcessor getVoProcessor();
}
