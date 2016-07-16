package org.web.service;

import java.util.List;

import org.web.exception.VoProcessorException;

/**
 * 对象适配器
 * @author mastery
 * @Time 2015-4-11 上午12:45:21
 * 
 */
public interface ObjectAdapter {
	
	public List<Object> getVo(List<Object> vos) throws VoProcessorException ;
	
	/**
	 * 是否是逆向的
	 * @param isReverse
	 */
	public void setReverse(boolean isReverse);
}
