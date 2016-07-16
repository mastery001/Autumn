package org.web.dao.core.support;

import java.util.List;

import org.web.dao.core.DaoAdvice;
import org.web.exception.DBException;

class LogicHelper {
	
	/**
	 * 在保存数据之前先查询该数据是否存在
	 * @param dao
	 * @param obj
	 * @return
	 * @throws DBException
	 */
	public Object saveBeforeQuery(DaoAdvice dao , Object obj) throws DBException {
		List<Object> list = dao.query(obj.getClass(),
				obj, null, false);
		if (list.size() == 0) {
			dao.save(obj);
			list = dao.query(obj.getClass(), obj, null,
					false);
		}
		return list.get(0);
	}
}
