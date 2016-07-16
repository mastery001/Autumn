package org.web.framework.service;

import java.util.List;

import org.cache.framework.Cache;
import org.cache.framework.LFUCache;
import org.web.exception.DBException;

/**
 * 缓存控制者，用于控制缓存的策略
 * 
 * @author mastery
 * @Time 2015-3-14 下午11:48:13
 * 
 */
public interface CacheMaster {

	public static final Cache<Class<?>, List<Object>> CACHE = new LFUCache<Class<?>, List<Object>>(
			10, 1000 * 600);

	public List<Object> controlStrategy(Class<?> entityClass) throws DBException;

}
