package org.web.dao.core.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.database.db.DBUtil;
import org.web.dao.annotation.AnnotationUtil;
import org.web.dao.core.SqlAdvice;
import org.web.dao.core.SqlConstant;
import org.web.dao.core.help.ColumnMeta;
import org.web.dao.core.help.Condition;
import org.web.dao.core.help.DaoOptemplate;
import org.web.exception.DBException;

import tool.mastery.log.Logger;

public class AbstractPoDaoAdvice extends AbstractDaoAdvice {

	private static final Logger LOG = Logger
			.getLogger(AbstractPoDaoAdvice.class);

	private static final SqlAdvice sqlAdvice = GlobalSqlAdvice.getInstance();

	protected static final LogicHelper logicHelper = new LogicHelper();

	private String queryParams;

	public AbstractPoDaoAdvice() {
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(Object entity) throws DBException {
		try {
			AnnotationUtil annotationUtil = AnnotationUtil.getInstance();
			// 获取表名
			String tableName = annotationUtil.getAnnotationTableName(entity
					.getClass());
			// 获取主键名
			String[] primaryKeyNames = annotationUtil.getPrimaryKey(entity
					.getClass());
			// 获取实体类存放的属性名和值
			Map<String, Object> beanMap = annotationUtil.getBeanInfo(entity);
			/**
			 * select * from tablename where 1=0的意义 此语句的作用是:
			 * 打开此记录集,但并不从记录集中读取任何记录 直观点说就是,为保护记录集数据,仅做打开,是只做插入记录时用到
			 */
			List<ColumnMeta> list = DaoOptemplate.getInstance().refresh(
					tableName, conn);
			String sql = "insert into " + tableName;
			sql = sqlAdvice.buildSaveSql(sql, list, primaryKeyNames , beanMap);
			LOG.debug("current is execute save method, sql statement is :"
					+ sql);
			DaoOptemplate.getInstance().executeUpdate(conn, sql, (List<ColumnMeta>)beanMap.get("VALUE_LIST"), beanMap);
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.debug(e);
			throw new DBException(e.getMessage());
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void update(Object entity) throws DBException {
		// 用于记录执行sql返回的次数
		try {
			String sql = sqlAdvice.buildUpdateSql(entity, conn);
			Statement stmt = conn.createStatement();
			// TODO
			LOG.debug("current is execute update method, sql statement is :"
					+ sql);
			if (sql == null) {
				LOG.debug("current is execute update method, no field update, so return ");
				return;
			}
			// sqlAdvice.print(sql);
			stmt.executeUpdate(sql);
			DBUtil.close(stmt);
		} catch (SQLException e) {
			LOG.debug(e.getMessage(), e);
			throw new DBException(e);
		}
	}

	@Override
	public void delete(Object entity) throws DBException {
		try {
			String sql = sqlAdvice.buildDeleteSql(entity ,conn);
			LOG.debug("current is execute delete method, sql statement is :"
					+ sql);
			conn.createStatement().executeUpdate(sql);
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
			throw new DBException(e.getMessage());
		}
	}

	@Override
	public List<Object> query(Class<?> entityClass, Object entity, Page page,
			boolean flag) throws DBException {
		Condition condition = new Condition(entity);
		if (page == null) {
			page = new Page();
		}
		return this.queryForSingle(entityClass, page.getFirstIndex(),
				page.getMaxSize(), null, condition.getCondition(),
				condition.getValue(), flag);
	}

	/**
	 * 对数据库表的查询操作
	 * 
	 * @param entityClass
	 *            所需查询的实体类的Class类型
	 * @param firstIndex
	 *            页面索引
	 * @param maxResult
	 *            一页显示多少条记录
	 * @param OrderBy
	 *            OrderBy按什么来排序
	 * @param where_sql
	 *            查询条件(多个查询条件以逗号隔开)
	 * @param whereValue
	 *            对应查询条件的值(需与查询条件的顺序对应)
	 * @param flag
	 *            是否支持模糊查询，true代表支持，反之则不支持
	 * @return List<T> entityObject
	 * @throws DBException
	 */
	private List<Object> queryForSingle(Class<?> entityClass, int firstIndex,
			int maxResult, Map<String, String> OrderBy, String where_sql,
			String whereValue, boolean flag) throws DBException {
		List<Object> list = new ArrayList<Object>();
		String sql = "";
		try {
			sql = sqlAdvice.buildConditionSql(entityClass, firstIndex,
					maxResult, OrderBy, where_sql, whereValue, flag);
		} catch (SQLException e) {
			LOG.debug(e.getMessage(), e);
			throw new DBException(e);
		}
		list = list(sql, entityClass);
		return list;
	}

	private List<Object> list(String sql, Class<?> entityClass)
			throws DBException {
		return getResult(sql, entityClass);
	}

	private List<Object> getResult(String sql, Class<?> entityClass)
			throws DBException {
		List<Object> list = new ArrayList<Object>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			String frontSql = sqlAdvice.buildQuerySql(entityClass, queryParams,
					conn);
			String[] splitSql = frontSql.split(SqlConstant.ORDERBY_SPLIT);
			String completeSql = "";
			if (splitSql.length == 2) {
				completeSql = splitSql[0] + sql + splitSql[1];
			} else
				completeSql = splitSql[0] + sql;
			LOG.debug("current is execute single table query method, sql statement is :"
					+ completeSql);
			// sqlAdvice.print(completeSql);
			// 获取结果集
			rs = stmt.executeQuery(completeSql);
			list = helpAdvice.convertDataToObject(rs, entityClass);
			DBUtil.close(stmt);
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
			throw new DBException(e.getMessage());
		} finally {
			queryParams = null;
		}
		return list;
	}

	/**
	 * 根据主键id和实体类 加载一个实体对象 若未传递主键id，则查出的数据为数据库中的第一条数据
	 * 
	 * @throws DBException
	 */
	public Object get(Object entity) throws DBException {
		Object obj = null;
		if (entity != null) {
			String sql = "";
			try {
				sql = sqlAdvice.buildPrimaryConditionSql(entity);
			} catch (SQLException e) {
				throw new DBException(e);
			}
			if (sql.toLowerCase().indexOf("where") == -1) {
				return null;
			}
			List<Object> list = this.list(sql, entity.getClass());
			if (list != null && list.size() > 0) {
				obj = list.get(0);
			}
		}
		return obj;
	}

	@Override
	public boolean containsEntity(Object entity) throws DBException {
		if (entity != null) {
			String[] primaryKeys = AnnotationUtil.ANNOTAION_UTIL
					.getPrimaryKey(entity.getClass());
			StringBuilder sb = new StringBuilder();
			for (String primaryKey : primaryKeys) {
				sb.append(primaryKey + ",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			this.queryParams = sb.toString();
			String sql = "";
			try {
				sql = sqlAdvice.buildPrimaryConditionSql(entity);
			} catch (SQLException e) {
				LOG.debug(e.getMessage(), e);
				throw new DBException(e);
			}
			if (sql.toLowerCase().indexOf("where") == -1) {
				queryParams = null;
				return false;
			}
			List<Object> list = this.list(sql, entity.getClass());
			if (list != null && list.size() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 在保存数据之前先查询该数据是否存在，若是不存在则先保存至数据库中
	 * 
	 * @param obj
	 * @return
	 * @throws DBException
	 */
	public Object saveBeforeQuery(Object obj) throws DBException {
		return logicHelper.saveBeforeQuery(this, obj);
	}

}
