package org.web.framework.support;

import org.web.dao.core.DaoAdvice;
import org.web.service.ObjectAdapter;
import org.web.service.OperateServiceExecuteAdvice;

/**
 * beans.xml中的配置信息
 * 
 * @author mastery
 * @Time 2015-4-10 下午10:05:52
 * 
 */
public class BeansConfig implements Cloneable{

	public static final BeansConfig DEFAULT_OBJECT = DefaultBeansConfig.getInstance();

	private String name;

	private OperateServiceExecuteAdvice service;

	private DaoAdvice dao;

	private ObjectAdapter adapter;

	public OperateServiceExecuteAdvice getService() {
		if (service == null) {
			return DefaultBeansConfig.getInstance().getService();
		}
		return service;
	}

	public void setService(OperateServiceExecuteAdvice service) {
		this.service = service;
	}

	public DaoAdvice getDao() {
		if (service == null) {
			return DefaultBeansConfig.getInstance().getDao();
		}
		return dao;
	}

	public void setDao(DaoAdvice dao) {
		this.dao = dao;
	}

	public ObjectAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ObjectAdapter adapter) {
		this.adapter = adapter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BeansConfig [name=" + name + ", service=" + service + ", dao="
				+ dao + ", adapter=" + adapter + "]";
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
}
