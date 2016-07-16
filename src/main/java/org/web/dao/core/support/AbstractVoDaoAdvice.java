package org.web.dao.core.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.database.db.DBUtil;
import org.web.dao.core.SqlConstant;
import org.web.dao.core.help.Condition;
import org.web.dao.core.help.SqlCache;
import org.web.exception.DBException;
import org.web.exception.VoProcessorException;

import tool.mastery.core.BeanUtil;
import tool.mastery.log.Logger;

public abstract class AbstractVoDaoAdvice extends AbstractPoDaoAdvice {

	private static final Logger LOG = Logger.getLogger(AbstractVoDaoAdvice.class);
	
	protected VoResolve voResolve;

	protected final AbstractPoDaoAdvice DAO = new AbstractPoDaoAdvice();

	public AbstractVoDaoAdvice() {
		open();
		voResolve = buildVoResolve();
	}

	@Override
	public void open() {
		if(this.conn == null) {
			super.open();
			DAO.setConnection(conn);
		}
	}
	
	@Override
	public void close() {
		super.close();
		DAO.close();
	}

	@Override
	public void save(Object entity) throws DBException {
		save(initVoResolveToGetPoValue(entity));
	}

	@Override
	public void update(Object entity) throws DBException {
		update(initVoResolveToGetPoValue(entity));
	}

	
	@Override
	public void delete(Object entity) throws DBException {
		delete(initVoResolveToGetPoValue(entity));
	}

	protected Object[] initVoResolveToGetPoValue(Object entity) {
		helpAdvice.convertVoToPo(voResolve, entity);
		return voResolve.getPoObject();
	}

	protected void save(Object[] poValue) throws DBException {
		for (int i = 0; i < poValue.length; i++) {
			Object obj = super.get(poValue[i]);
			if (obj != null) {
				throw new DBException("此条数据已经存在，不能重复插入！");
			}
			super.save(poValue[i]);
		}
	}

	protected void update(Object[] poValue) throws DBException{
		for (Object po : poValue) {
			if (needPo(po)) {
				super.update(po);
			}
		}
	}

	protected void delete(Object[] poValue) throws DBException {
		for (Object po : poValue) {
			if (needPo(po)) {
				super.delete(po);
			}
		}
	}
	
	/** 
	* @Title: needPo 
	* @Description: 判断该PO是否需要 
	* @param @param entity
	* @param @return   
	* @return boolean    返回类型 
	* @throws 
	*/ 
	private boolean needPo(Object entity) {
		Class<?>[] np = whenUpdateOrDeleteUnNecessaryClasses();
		for (int i = 0; i < np.length; i++) {
			if (entity.getClass() == np[i]) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public List<Object> query(Class<?> entityClass, Object entity, Page page,
			boolean flag) throws DBException {
		List<Object> list = null;
		try {
			// 获得未加上查询条件的sql语句
			String initSql = SqlCache.getVoSql(helpAdvice, voResolve);
			// 获得完整的sql语句
			String sql = this.getSql(initSql, voResolve.getVoClass(), page,
					entity, flag);
			String condition = addWhereOrOrderCondition();
			if(condition != null) {
				sql += condition;
			}
			LOG.debug(
					"current is execute multi table query method, sql statement is :"
							+ sql);
			// sqlAdvice.print(sql);
			list = this.getResult(sql, voResolve.getVoClass());

		} catch (VoProcessorException e) {
			LOG.debug(e.getMessage() , e);
			throw new DBException(e.getMessage());
		}
		return list;

	}

	protected String addWhereOrOrderCondition() {
		return null;
	}

	public List<Object> getResult(String sql, Class<?> entityClass)
			throws DBException {
		List<Object> list = new ArrayList<Object>();
		PreparedStatement pstmt = DBUtil.getPstmt(conn, sql);
		ResultSet rs = DBUtil.getRs(pstmt);
		try {
			list = helpAdvice.convertDataToObject(rs, entityClass);
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
		}
		return list;
	}

	/**
	 * // 获得完整的sql语句,添加了条件的
	 * 
	 * @param initSql
	 * @param entityClass
	 * @param page
	 * @param vo
	 * @param flag
	 * @return
	 */
	protected String getSql(String initSql, Class<?> entityClass, Page page,
			Object vo, boolean flag) {
		StringBuilder sqlBuilder = new StringBuilder(initSql);
		Condition condition = new Condition(vo);
		if (condition.getCondition() != null) {
			// 若已经包含了where条件
			if (sqlBuilder.indexOf("where") >= 0
					|| sqlBuilder.indexOf("WHERE") >= 0) {
				sqlBuilder.append(" and ");
			} else {
				sqlBuilder.append(" where ");
			}
			String[] conditions = condition.getCondition().split(
					SqlConstant.CONDITION_SPLIT);
			String[] values = condition.getValue().split(
					SqlConstant.CONDITION_SPLIT);
			// 获得查询条件
			for (int i = 0; i < conditions.length; i++) {
				sqlBuilder.append(this.getSurplusSql(initSql, entityClass,
						conditions[i], values[i], flag));
				sqlBuilder.append(" and ");
			}
			// 将多余的and删除
			sqlBuilder.delete(sqlBuilder.lastIndexOf("and"),
					sqlBuilder.length());
		}
		return sqlBuilder.toString();
	}

	/**
	 * 通过在sql语句中寻找真实的查询条件
	 * 
	 * @param sql
	 * @param condition
	 * @return
	 */
	private String getSurplusSql(String sql, Class<?> entityClass,
			String condition, String value, boolean flag) {
		String surplusSql = "";
		String truthCondition = "";
		String[] splitSql = sql.split(" ");
		for (int i = 0; i < splitSql.length; i++) {
			boolean flag1 = false;
			String[] splitAgainSql = splitSql[i].split(",");
			for (int j = 0; j < splitAgainSql.length; j++) {
				if (splitAgainSql[j].indexOf(condition) >= 0) {
					truthCondition = splitAgainSql[j];
					flag1 = true;
					break;
				}
			}
			if (flag1) {
				break;
			}
		}

		if (BeanUtil.isNumber(entityClass, condition)) {
			surplusSql = truthCondition + " =" + value;
		} else {
			if (flag) {
				surplusSql = truthCondition + " like '%" + value + "%'";
			} else {
				surplusSql = truthCondition + " ='" + value + "'";
			}

		}
		return surplusSql;
	}

	/**
	 * 创建vo分解对象
	 */
	protected abstract VoResolve buildVoResolve();

	/**
	 * 判定哪些对象需要操作
	 * 
	 * @param obj
	 * @return
	 */
	@Deprecated
	protected boolean operateCondition(Object obj) {
		return false;
	}
	
	/** 
	* @Title: whenUpdateOrDeleteNeedObject 
	* @Description: 当修改或删除时不需要的对象 
	* @param @return   
	* @return Object    返回类型 
	* @throws 
	*/ 
	protected abstract Class<?>[] whenUpdateOrDeleteUnNecessaryClasses();
}
