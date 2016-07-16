package org.web.framework.support;

import java.util.List;

import org.web.exception.DBException;
import org.web.framework.service.CacheMaster;

public class DefaultCacheMaster implements CacheMaster {

	@Override
	public List<Object> controlStrategy(Class<?> entityClass) throws DBException {
		/*
		 * List<Object> list = CACHE.get(key); if (list == null) { list =
		 * resultAdvice.getResult(key, entityClass); CACHE.put(key, list); }
		 * return list;
		 */
		return null;
	}
}
