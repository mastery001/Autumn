package org.web.framework.support;

import org.web.dao.annotation.AnnotationUtil;
import org.web.dao.core.support.AbstractDaoAdvice;
import org.web.dao.core.support.ProxyDaoAdvice;
import org.web.exception.BeanInitializationException;
import org.web.service.AbstractService;

import tool.mastery.core.ClassUtil;

/**
 * dao和service的枚举接口，用于获取对应的dao和service
 * 
 * @author mastery
 * @Time 2015-4-1 下午1:41:41
 * 
 */
public enum EnumBean {
	Dao {
		@Override
		public Object get(BeansConfig das) throws BeanInitializationException {
			Class<?> entityClass = ClassUtil.getClassByClassName(das.getName());
			// 若是vo的话，判断返回的是否是默认的dao，若是话测抛出异常给前台，否则向下执行
			if (AnnotationUtil.ANNOTAION_UTIL
					.getAnnotationTableName(entityClass) == null) {
				if (das.getDao() == BeansConfig.DEFAULT_OBJECT.getDao()) {
					throw new BeanInitializationException(das.getName()
							+ "对应的类尚未配置dao层对象！请检查beans.xml配置文件！");
				}
			}
			AbstractDaoAdvice ada = (AbstractDaoAdvice)das.getDao();
			ada.setEntityClass(entityClass);
			return new ProxyDaoAdvice(ada);
		}
	},
	Service {
		@Override
		public Object get(BeansConfig das) throws BeanInitializationException {
			AbstractService service = (AbstractService)das.getService();
			service.setName(das.getName());
			return service;
		}
	},Adapter{
		@Override
		Object get(BeansConfig das) throws BeanInitializationException {
			return das.getAdapter();
		}
		
	};
	private EnumBean() {
	}

	// 用于获取对应的dao和service
	abstract Object get(BeansConfig das)
			throws BeanInitializationException;

}
