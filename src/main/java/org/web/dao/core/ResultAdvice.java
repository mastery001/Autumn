package org.web.dao.core;

import java.util.List;

import org.web.exception.DBException;
import org.web.framework.service.Advice;

/**
 * 提供查询结果的一项服务
 * @author mastery
 * @Time 2015-3-16 下午5:25:35
 * 
 */
public interface ResultAdvice extends Advice{
	
	List<Object> getResult(String sql , Class<?> eneityClass) throws DBException;

}
