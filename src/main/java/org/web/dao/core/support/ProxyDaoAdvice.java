package org.web.dao.core.support;

import java.util.List;

import org.web.dao.core.DaoAdvice;
import org.web.exception.DBException;

public class ProxyDaoAdvice implements DaoAdvice {

	private DaoAdvice advice;

	public ProxyDaoAdvice(DaoAdvice advice) {
		this.advice = advice;
	}
/*
	@Override
	public List<Object> getResult(String sql, Class<?> eneityClass)
			throws DBException {
		open();
		try {
			return this.advice.getResult(sql, eneityClass);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}*/

	@Override
	public void save(Object entity) throws DBException {
		open();
		try {
			this.advice.save(entity);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}

	@Override
	public void update(Object entity) throws DBException {
		open();
		try {
			this.advice.update(entity);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}

	@Override
	public void delete(Object entity) throws DBException {
		open();
		try {
			this.advice.delete(entity);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}

	@Override
	public List<Object> query(Class<?> entityClass, Object entity, Page page,
			boolean flag) throws DBException {
		open();
		try {
			return this.advice.query(entityClass, entity, page, flag);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}

	@Override
	public Object get(Object entity) throws DBException {
		open();
		try {
			return this.advice.get(entity);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}

	@Override
	public boolean containsEntity(Object entity) throws DBException {
		open();
		try {
			return this.advice.containsEntity(entity);
		} catch (DBException e) {
			throw e;
		} finally {
			close();
		}
	}

	@Override
	public void open() {
		advice.open();
	}

	@Override
	public void close() {
		advice.close();
	}

}
