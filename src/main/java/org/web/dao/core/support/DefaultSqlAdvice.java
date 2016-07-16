package org.web.dao.core.support;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.web.dao.annotation.AnnotationUtil;
import org.web.dao.annotation.Util;
import org.web.dao.core.SqlAdvice;
import org.web.dao.core.SqlConstant;
import org.web.dao.core.help.ColumnMeta;
import org.web.dao.core.help.DaoOptemplate;
import org.web.dao.core.help.SqlCache;

class DefaultSqlAdvice implements SqlAdvice {

	/**
	 * 允许打印sql语句
	 */
	private boolean isPrint = true;

	@Override
	public boolean isPrint() {
		return isPrint;
	}

	@Override
	public void print(Object obj) {
		if (isPrint) {
			System.out.println(obj);
		}
	}

	@Override
	public String buildSaveSql(String sql, List<ColumnMeta> list,
			String[] primaryKeyNames, Map<String, Object> beanMap ) throws SQLException {
		String insertSql = "";
		// 用于保存值不为null的字段
		List<ColumnMeta> valueList = new ArrayList<ColumnMeta>();
		// 获得表中一共有几个字段
		int columnCount = list.size();
		// 组装字段
		StringBuffer columnBuffer = new StringBuffer(sql + "(");
		StringBuffer valueBuffer = new StringBuffer(" values(");
		for (int i = 0; i < columnCount; i++) {
			ColumnMeta cm = list.get(i);
			// 获取列名
			String columnName = cm.getColumnName();
			if(beanMap.get(columnName) == null) {
				continue;
			}
			valueList.add(cm);
			columnBuffer.append(columnName + ",");
			if (cm.isAutoIncrement()) {
				valueBuffer.append("null,");
			} else {
				valueBuffer.append("?,");
			}
		}
		beanMap.put("VALUE_LIST", valueList);
		columnBuffer.deleteCharAt(columnBuffer.lastIndexOf(","));
		columnBuffer.append(")");
		valueBuffer.deleteCharAt(valueBuffer.lastIndexOf(","));
		valueBuffer.append(")");
		insertSql = columnBuffer.toString() + valueBuffer.toString();
		return insertSql;
	}

	@Override
	public String buildUpdateSql(Object entity, Connection conn)
			throws SQLException {
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
		 * select * from tablename where 1=0的意义 此语句的作用是: 打开此记录集,但并不从记录集中读取任何记录
		 * 直观点说就是,为保护记录集数据,仅做打开,是只做插入记录时用到
		 */
		List<ColumnMeta> list = DaoOptemplate.getInstance().refresh(tableName,
				conn);

		int columnCount = list.size();

		StringBuffer valueBuffer = new StringBuffer("update " + tableName
				+ " set ");
		String whereCondition = "";
		/**
		 * 此处用于判断实体的存在性 若存在则SQL语句组装完成，将where条件中的id补入
		 */
		String word = " where ";
		boolean flag = false;
		for (int i = 0; i < columnCount; i++) {
			ColumnMeta cm = list.get(i);
			String columnName = cm.getColumnName();
			String columnType = cm.getColumnType();
			Object columnValue = beanMap.get(columnName);
			if (columnValue == null) {
				continue;
			}
			if (isPrimaryKey(columnName, primaryKeyNames)) {
				whereCondition += word + getConditionSyntax(columnName, columnType, columnValue);
				if (primaryKeyNames.length > 1) {
					word = " and ";
				}
			} else {
				if (columnValue != null) {
					flag = true;
					// 若为最后一个
					if (i == columnCount) {
						valueBuffer.append(getConditionSyntax(columnName, columnType, columnValue));
					} else {
						valueBuffer.append(getConditionSyntax(columnName, columnType, columnValue) + ",");
					}
				}
			}
		}
		if (!flag) {
			return null;
		}
		// 获取最后一个逗号到总长度的大小
		int quoteApartLength = valueBuffer.length()
				- valueBuffer.toString().lastIndexOf(",");
		if (quoteApartLength == 1) {
			valueBuffer.deleteCharAt(valueBuffer.toString().lastIndexOf(","));
		}
		String sql = valueBuffer.toString() + whereCondition;
		return sql;
	}

	private boolean isNumberType(String columnType) {
		return columnType.indexOf("int") == 0
				|| columnType.indexOf("integer") == 0
				|| columnType.indexOf("float") == 0
				|| columnType.indexOf("double") == 0
				|| columnType.indexOf("nember") == 0
				|| columnType.indexOf("numeric") == 0;
	}

	/**
	 * 判断列是否为主键
	 * 
	 * @param columnName
	 * @param primaryKeyNames
	 * @return
	 */
	private boolean isPrimaryKey(String columnName, String[] primaryKeyNames) {
		for (int i = 0; i < primaryKeyNames.length; i++) {
			if (columnName.equals(primaryKeyNames[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String buildDeleteSql(Object entity, Connection conn)
			throws SQLException, Exception {
		AnnotationUtil annotationUtil = AnnotationUtil.getInstance();
		// 获取表名
		String tableName = annotationUtil.getAnnotationTableName(entity
				.getClass());
		// 获取主键名
		String[] primaryKeyNames = annotationUtil.getPrimaryKey(entity
				.getClass());
		// 获取实体类存放的属性名和值
		Map<String, Object> beanMap = annotationUtil.getBeanInfo(entity);
		String sql = "delete from " + tableName;
		String whereCondition = "";
		boolean flag = false;
		/**
		 * 此处用于判断实体的存在性 若存在则SQL语句组装完成，将where条件中的id补入
		 */
		String word = " where ";
		for (String columnName : primaryKeyNames) {
			String columnType = entity.getClass().getDeclaredField(columnName)
					.getType().getSimpleName();
			Object columnValue = beanMap.get(columnName);
			if (columnValue == null) {
				flag = true;
				continue;
			}
			whereCondition += word + getConditionSyntax(columnName, columnType, columnValue);
			if (primaryKeyNames.length > 1) {
				word = " and ";
			}
		}
		// 如果主键的值为null，则通过普通条件查询
		if(flag) {
			List<ColumnMeta> list = DaoOptemplate.getInstance().refresh(tableName,
					conn);
			int columnCount = list.size();
			for(int i = 0 ; i < columnCount; i ++) {
				ColumnMeta cm = list.get(i);
				String columnName = cm.getColumnName();
				if(isPrimaryKey(columnName, primaryKeyNames)) {
					continue;
				}
				String columnType = cm.getColumnType();
				Object columnValue = beanMap.get(columnName);
				if (columnValue == null) {
					continue;
				}
				whereCondition += word + getConditionSyntax(columnName, columnType, columnValue);
				if (whereCondition.contains("where")) {
					word = " and ";
				}
			}
		}
		if (!whereCondition.contains("where")) {
			throw new SQLException("查询的主键的值不能为空！");
		}
		sql += whereCondition;
		return sql;
	}

	/** 
	* @Title: buildConditionSyntax 
	* @Description: 得到形如 u_id = "123"的语句 
	* @param @param columnName
	* @param @param columnType
	* @param @param columnValue
	* @param @return   
	* @return String    返回类型 
	* @throws 
	*/ 
	private String getConditionSyntax(String columnName, String columnType,
			Object columnValue) {
		String condition = "";
		// 当主键为数字的类型时
		if (isNumberType(columnType)) {
			condition = columnName + "=" + columnValue;
		} else {
			if (columnType.indexOf("date") == 0) {
				columnValue = new Util()
						.transferDateToString((java.util.Date) columnValue);
			}
			condition = columnName + "='" + columnValue + "'";
		}
		return condition;
	}

	@Override
	public void allowPrintSql(boolean isPrint) {
		this.isPrint = isPrint;
	}

	@Override
	public String buildQuerySql(Class<?> entityClass, Connection conn)
			throws SQLException {
		if (!SqlCache.VOSQL.containsKey(entityClass)) {
			String orderBy = "";
			AnnotationUtil annotationUtil = AnnotationUtil.getInstance();
			// 获取表名
			String tableName = annotationUtil
					.getAnnotationTableName(entityClass);
			List<ColumnMeta> list = DaoOptemplate.getInstance().refresh(
					tableName, conn);
			StringBuilder sb = new StringBuilder("select ");
			for (int i = 0; i < list.size(); i++) {
				ColumnMeta cm = list.get(i);
				String columnName = cm.getColumnName();
				sb.append(columnName + ",");
				// 如果是自动增长,或者是日期,则设置自动降序排列
				if (orderBy.equalsIgnoreCase("")) {
					if (cm.isAutoIncrement()
							|| cm.getColumnType().indexOf("date") != -1) {
						orderBy = " order by " + columnName + " desc";
					}
				}
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append(" from " + tableName);
			sb.append(SqlConstant.ORDERBY_SPLIT + orderBy);
			SqlCache.VOSQL.put(entityClass, sb.toString());
		}

		return SqlCache.VOSQL.get(entityClass);
	}

	@Override
	public String buildQuerySql(Class<?> entityClass, String queryParams,
			Connection conn) throws SQLException {
		String sql = this.buildQuerySql(entityClass, conn);
		if (queryParams == null) {
			return sql;
		}
		String[] fullSql = sql.split(" ");
		StringBuilder trueSQL = new StringBuilder(fullSql[0] + " "
				+ queryParams + " ");
		for (int i = 2; i < fullSql.length; i++) {
			trueSQL.append(fullSql[i] + " ");
		}
		return trueSQL.toString();
	}

	@Override
	public String buildPrimaryConditionSql(Object entity) throws SQLException {
		AnnotationUtil annotationUtil = AnnotationUtil.getInstance();
		Map<String, Object> primary = annotationUtil.getPrimaryValue(entity);
		String sql = "";
		Map<String, PropertyDescriptor> beanMap = null;
		try {
			beanMap = annotationUtil.getBeanInfo(entity.getClass());
		} catch (IntrospectionException e1) {
			throw new SQLException("查询失败:" + e1.getMessage());
		}
		Set<String> primaryNames = primary.keySet();
		String joinWord = " where ";
		for (Iterator<String> it = primaryNames.iterator(); it.hasNext();) {
			String primaryName = it.next();
			if (primary.get(primaryName) == null) {
				continue;
			}
			if (sql.indexOf("where") != -1) {
				joinWord = " and ";
			}
			// 获取主键的数据类型
			String typeName = beanMap.get(primaryName).getPropertyType()
					.getSimpleName().toLowerCase();
			// 若为字符类型
			sql += joinWord;
			if (typeName.equals("string")) {
				sql += primaryName + "='" + primary.get(primaryName) + "'";
			} else {
				sql += primaryName + "=" + primary.get(primaryName);
			}
		}
		return sql;
	}

	@Override
	public String buildConditionSql(Class<?> entityClass, int firstIndex,
			int maxResult, Map<String, String> OrderBy, String where_sql,
			String whereValue, boolean flag) throws SQLException {
		String sql = null;
		AnnotationUtil annotationUtil = AnnotationUtil.getInstance();
		Map<String, PropertyDescriptor> beanMap = null;
		try {
			beanMap = annotationUtil.getBeanInfo(entityClass);
		} catch (IntrospectionException e1) {
			throw new SQLException("查询失败：" + e1.getMessage());
		}
		// "select * from " + tableName
		// 拼接查询sql语句
		StringBuffer sqlBuffer = new StringBuffer();
		// 如果有条件查询
		if (where_sql != null) {
			sqlBuffer.append(" where ");
			String[] splitWhereSql = where_sql
					.split(SqlConstant.CONDITION_SPLIT);
			String[] splitWhereValue = whereValue
					.split(SqlConstant.CONDITION_SPLIT);
			// 如果支持模糊查询
			if (flag) {
				for (int i = 0; i < splitWhereSql.length; i++) {
					// 获取此参数的类型名
					String typeName = beanMap.get(splitWhereSql[i])
							.getPropertyType().getSimpleName().toLowerCase();
					if (typeName.equals("string")
							|| typeName.indexOf("date") != -1) {
						sqlBuffer.append(
								splitWhereSql[i] + " like '%"
										+ splitWhereValue[i] + "%'").append(
								" and ");
					} else {
						sqlBuffer.append(
								splitWhereSql[i] + "=" + splitWhereValue[i])
								.append(" and ");
					}
				}
			} else {
				for (int i = 0; i < splitWhereSql.length; i++) {
					// 获取此参数的类型名
					String typeName = beanMap.get(splitWhereSql[i])
							.getPropertyType().getSimpleName().toLowerCase();
					if (typeName.equals("string")
							|| typeName.indexOf("date") != -1) {
						sqlBuffer.append(
								splitWhereSql[i] + "='" + splitWhereValue[i]
										+ "'").append(" and ");
					} else {
						sqlBuffer.append(
								splitWhereSql[i] + "=" + splitWhereValue[i])
								.append(" and ");
					}

				}
			}
			// 将多余的and删除
			sqlBuffer.delete(sqlBuffer.lastIndexOf("and"), sqlBuffer.length());

		}

		// 设置按主键降序排序
		// sqlBuffer.append(" order by " + primaryKeyName + " desc ");
		/*
		 * if (firstIndex == 0 && maxResult == 0) {
		 * sqlBuffer.append(" order by " + primaryKeyName + " desc "); } else {
		 * sqlBuffer.append(" order by " + primaryKeyName + " desc limit " +
		 * firstIndex + "," + maxResult); }
		 */
		sql = sqlBuffer.toString();
		return sql;
	}

}
