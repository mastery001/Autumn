package org.web.dao.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.web.dao.core.help.ColumnMeta;

/**
 * 构建增删改查的sql语句,对sql语句提供服务的类
 * @author mastery
 * @Time 2015-3-26 下午2:47:29
 * 
 */
public interface SqlAdvice extends PrintAdvice {
	
	/**
	 * 构建保存sql语句
	 * @param beanMap 
	 * @param entity
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	String buildSaveSql(String sql , List<ColumnMeta> list , String[] primaryKeyNames, Map<String, Object> beanMap ) throws SQLException;

	/**
	 * 构建修改sql语句
	 * @param entity
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	String buildUpdateSql(Object entity, Connection conn) throws SQLException;

	/**
	 * 构建删除sql语句
	 * @param entity
	 * @param stmt
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	String buildDeleteSql(Object entity , Connection conn) throws SQLException,
			Exception;

	/**
	 * 构建查询sql语句
	 * @param entityClass	查询的类的字节码
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	String buildQuerySql(Class<?> entityClass, Connection conn) throws SQLException;
	
	/**
	 * 构建查询sql语句
	 * @param entityClass
	 * @param queryParams	需要的查询参数，需要用逗号隔开
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	String buildQuerySql(Class<?> entityClass, String queryParams, Connection conn) throws SQLException;
	
	/**
	 * 构建where语句后的句子，且构建的是主键
	 * @param entity
	 * @return
	 * @throws SQLException
	 */
	String buildPrimaryConditionSql(Object entity) throws SQLException;
	
	/**
	 * 构建where语句后的句子，
	 * @param entityClass
	 * @param firstIndex
	 * @param maxResult
	 * @param OrderBy
	 * @param where_sql
	 * @param whereValue
	 * @param flag
	 * @return
	 * @throws SQLException
	 */
	String buildConditionSql(Class<?> entityClass, int firstIndex,
			int maxResult, Map<String, String> OrderBy, String where_sql,
			String whereValue, boolean flag) throws SQLException;

	/**
	 * @param isPrint
	 */
	void allowPrintSql(boolean isPrint);

}
