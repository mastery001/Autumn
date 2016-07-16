package org.web.dao.core;

import java.util.List;

import org.web.dao.core.support.Page;
import org.web.exception.DBException;

/**
 * 负责对数据库提供操作的接口服务
 * @author Administrator
 *
 */
public interface DaoAdvice{
	
	void open();
	
	/**
	 * 统一的保存方法
	 * @param entity
	 * @return
	 * @throws DBException
	 */
	void save(Object entity) throws DBException;
	
	/**
	 * 统一的修改方法
	 * @param entity
	 * @return
	 * @throws DBException
	 */
	void update(Object entity) throws DBException;
	
	/**
	 * 统一的删除方法
	 * @param entity
	 * @return
	 * @throws DBException
	 */
	void delete(Object entity) throws DBException;
	
	/**
	 * 根据实体对象类字节码，以及查询条件来查询
	 * @param entityClass	需要查询的对象类字节码
	 * @param entity		查询条件封装的对象
	 * @param page			分页对象
	 * @param flag			是否支持模糊查询
	 * @return
	 * @throws DBException
	 */
	List<Object> query(Class<?> entityClass , Object entity , Page page , boolean flag) throws DBException;
	
	/**
	 * 根据主键id和实体类 加载一个实体对象
	 *  若未传递主键id，则查出的数据为数据库中的第一条数据
	 * @param entity
	 * @return
	 * @throws DBException
	 */
	Object get(Object entity) throws DBException;
	
	/**
	 * 判断该实体对象是否在数据库有记录,只会根据主键进行索引确定
	 * @param entity
	 * @return
	 * @throws DBException
	 */
	boolean containsEntity(Object entity) throws DBException;
	
	void close();
	
}
