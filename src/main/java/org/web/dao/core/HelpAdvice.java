package org.web.dao.core;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.web.dao.core.support.VoResolve;

/**
 * 对数据库的操作提供帮助的服务,以及对vo的拆分，和对获取vo的sql语句的帮助类
 * 
 * @author mastery
 * @Time 2015-3-16 下午5:33:36
 * 
 */
public interface HelpAdvice {

	/**
	 * 将数据库中的值转换成实体类对象
	 * 
	 * @param rs
	 * @param entityClass
	 * @return
	 */
	List<Object> convertDataToObject(ResultSet rs, Class<?> entityClass)
			throws IllegalArgumentException, SQLException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, IntrospectionException;

	/**
	 * 获得vo分解对象
	 * 
	 * @param allPo
	 *            构成vo的所有po
	 * @param voClass
	 *            需要分解的vo
	 * @param needPoObjectClass
	 *            需要的po
	 * @return
	 */
	public VoResolve getVoResolve(Class<?>[] allPo, Class<?> voClass,
			Class<?>[] needPoObjectClass);

	/**
	 * 获得vo分解对象
	 * 
	 * @param allPo
	 *            构成vo的所有po
	 * @param voClass
	 *            需要分解的vo
	 * @param needPoObjectClass
	 *            需要的po
	 * @param ignoreField
	 *            忽略的字段，如果有多个则以逗号隔开
	 * @return
	 */
	public VoResolve getVoResolve(Class<?>[] allPo, Class<?> voClass,
			Class<?>[] needPoObjectClass, Map<Class<?>, String> ignoreField);

	/**
	 * 将vo对象的值转换到对应的po对象中
	 * 
	 * @param voResolve
	 * @param entity
	 */
	public void convertVoToPo(VoResolve voResolve, Object entity);

	public String getVoSql(VoResolve voResolve);

}
