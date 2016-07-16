package org.web.service;

import java.util.List;

import org.web.access.factory.DaoAdviceFactory;
import org.web.dao.core.DaoAdvice;
import org.web.exception.BeanInitializationException;
import org.web.exception.DBException;
import org.web.exception.ErrorException;

import tool.mastery.core.CollectionUtil;
import tool.mastery.log.Logger;

/**
 * 对数据增删改的统一处理类 ,
 * 
 * @author mastery
 * @Time 2015-3-8 下午11:47:48
 * 
 */
public abstract class OperateService extends AbstractService implements
		OperateServiceExecuteAdvice {

	private static final Logger LOG = Logger.getLogger(OperateService.class);

	protected DaoAdvice dao;

	protected void add(List<Object> list) throws ErrorException {
		String errorMessage = "";
		for (int i = 0; i < list.size(); i++) {
			try {
				dao.save(list.get(i));
			} catch (DBException e) {
				LOG.debug(e.getMessage(), e);
				errorMessage += e.getMessage();
			}
		}
		if (!errorMessage.equals("")) {
			throw new ErrorException(errorMessage);
		}
	}

	protected void update(List<Object> list) throws ErrorException {
		String errorMessage = "";
		for (int i = 0; i < list.size(); i++) {
			try {
				dao.update(list.get(i));
			} catch (DBException e) {
				LOG.debug(e.getMessage(), e);
				errorMessage += e.getMessage();
			}
		}
		if (!errorMessage.equals("")) {
			throw new ErrorException(errorMessage);
		}
	}

	protected void delete(List<Object> list) throws ErrorException {
		String errorMessage = "";
		for (int i = 0; i < list.size(); i++) {
			try {
				dao.delete(list.get(i));
			} catch (DBException e) {
				LOG.debug(e.getMessage(), e);
				errorMessage += e.getMessage();
			}
		}
		if (!errorMessage.equals("")) {
			throw new ErrorException(errorMessage);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> void invokeMethod(Object entity, String operate)
			throws ErrorException, BeanInitializationException {
		List<Object> list = null;
		if(!(entity instanceof List)) {
			list = CollectionUtil.convertObjectToList(entity);
		}else {
			list = (List<Object>)entity;
		}
		list = processList(list);
		if("add".equals(operate)) {
			this.add(list);
		}else if("update".equals(operate)) {
			this.update(list);
		}else {
			this.delete(list);
		}
	}

	@Override
	public void execute(Object entity, String operate) throws ErrorException,
			BeanInitializationException {
		if (entity == null) {
			throw new NullPointerException("执行execute方法时entity参数不能为空");
		}
		dao = DaoAdviceFactory.getDao(name);
		invokeMethod(entity , operate);
	}

	

}
