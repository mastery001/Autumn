package org.web.dao.core.support;

import java.util.List;

import org.web.exception.DBException;

/**
 * 默认的操作dao，理论上为了支持单表的操作
 * 
 * @author mastery
 * @Time 2015-3-16 下午12:42:32
 * 
 */
public class DefaultDaoAdvice extends AbstractPoDaoAdvice {

	@Override
	public void save(Object entity) throws DBException {
		if (super.containsEntity(entity)) {
			throw new DBException("此条数据已经存在，不能重复插入！");
		}
		super.save(entity);
	}

	@Override
	public void update(Object entity) throws DBException {
		super.update(entity);
	}

	@Override
	public void delete(Object entity) throws DBException {
		super.delete(entity);
	}

	@Override
	public List<Object> query(Class<?> entityClass, Object entity, Page page,
			boolean flag) throws DBException {
		return super.query(entityClass, entity, page, flag);
	}

}
