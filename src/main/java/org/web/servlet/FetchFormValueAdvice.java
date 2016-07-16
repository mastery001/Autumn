package org.web.servlet;

import java.util.List;

import org.web.exception.ActionExecuteException;
import org.web.framework.service.Advice;

/**
 * 表单收集服务，提供对表单的收集
 * @author mastery
 * @Time 2015-3-17 下午3:31:54
 * 
 */
public interface FetchFormValueAdvice extends Advice{
	
	/**
	 * 获取单独上传文件时导入数据时得到的路径
	 * @return
	 * @throws ErrorException
	 */
	//String fetchUploadFilePath() throws ErrorException;
	
	/**
	 * 收集List表单，封装name对应实体对象的值的一组值
	 * @return
	 */
	List<Object> fetchFormListValue() throws ActionExecuteException;
	
	/**
	 * 收集一个对象，其中这个对象是name对应的实体对应的对象
	 * @return
	 */
	Object fetchFormObjectValue()  throws ActionExecuteException;
}
