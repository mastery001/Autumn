package org.web.service;

import java.util.List;

import org.web.exception.ErrorException;
import org.web.exception.VoProcessorException;
import org.web.framework.support.ConfigFactory;
import org.web.framework.support.EnumBean;

public abstract class AbstractService {

	// 设置需要执行的对象名
	protected String name;
	
	public AbstractService(String name) {
		this.name = name;
	}

	public AbstractService() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected List<Object> processList(List<Object> list)
			throws ErrorException {
		ObjectAdapter processor = (ObjectAdapter) new ConfigFactory(
				EnumBean.Adapter).getBean(name);
		if (processor != null) {
			try {
				processor.setReverse(!isOperateService(this.getClass()));
				list = processor.getVo(list);
			} catch (VoProcessorException e) {
				throw new ErrorException("对象处理失败！请检查原因",e);
			}
		}
		return list;
	}
	
	private boolean isOperateService(Class<?> cls) {
		if(cls == null) {
			return false;
		}
		// 如果是OperateService
		if(cls.getSimpleName().equalsIgnoreCase("OperateService")) {
			return true;
		}else {
			return isOperateService(cls.getSuperclass());
		}
	}
}
