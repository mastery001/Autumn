package org.web.dao.core.support;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.web.dao.annotation.AnnotationUtil;
import org.web.dao.annotation.Util;
import org.web.dao.core.HelpAdvice;

import tool.mastery.log.Logger;

public class DefaultHelpAdvice implements HelpAdvice {

	private static final Logger LOG = Logger.getLogger(DefaultHelpAdvice.class);

	@Override
	public List<Object> convertDataToObject(ResultSet rs, Class<?> entityClass)
			throws IllegalArgumentException, SQLException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, IntrospectionException {
		List<Object> list = new ArrayList<Object>();
		// 检索此ResultSetMetaData对象的列编号、类型和属性
		ResultSetMetaData rsmd = rs.getMetaData();
		// 获取所有列
		int columnCount = rsmd.getColumnCount();
		// 封装好的类型信息
		Map<String, PropertyDescriptor> beanMap = AnnotationUtil.getInstance()
				.getBeanInfo(entityClass);
		while (rs.next()) {
			Object bean = entityClass.newInstance();
			for (int i = 1; i <= columnCount; i++) {
				// 获取列名
				String columnName = rsmd.getColumnName(i).toLowerCase();
				// 获取列的值
				Object columnValue = rs.getObject(columnName);

				// 获得属性描述对象
				PropertyDescriptor pd = beanMap.get(columnName);
				if (pd == null) {
					LOG.debug("the field " + columnName + "'s type is null !");
					throw new SQLException("请检查" + columnName + "字段在"
							+ entityClass.getName() + "类中是否存在");
				}
				// 属性数据类型Integer,String,Float,Double(名称)
				String fieldType = pd.getPropertyType().getName();
				// 获取set方法
				Method writerMethod = pd.getWriteMethod();
				if (columnValue != null) {
					// 获取调用set方法的值
					Object args = new Util()
							.typeConvert(columnValue, fieldType);
					// 动态调用实体类的set方法，将值保存到实体类对象中
					try {
						writerMethod.invoke(bean, args);
					} catch (Exception e) {
						continue;
					}
				}
			}
			list.add(bean);
		}
		return list;
	}

	/**
	 * 获得vo分解对象
	 * 
	 * @param allPo
	 *            构成vo的所有po
	 * @param voClass
	 *            需要分解的vo
	 * @param needPoObjectClass
	 *            需要的po
	 * @return
	 */
	public VoResolve getVoResolve(Class<?>[] allPo, Class<?> voClass,
			Class<?>[] needPoObjectClass) {
		return this.getVoResolve(allPo, voClass, needPoObjectClass, null);
	}

	@Override
	public VoResolve getVoResolve(Class<?>[] allPo, Class<?> voClass,
			Class<?>[] needPoObjectClass, Map<Class<?>, String> ignoreField) {
		VoResolve voResolve = new VoResolve();
		voResolve.setVoClass(voClass);
		voResolve.setNeedPoObjectClass(needPoObjectClass);
		voResolve.setAllPo(allPo);
		if (ignoreField == null) {
			ignoreField = new HashMap<Class<?>, String>();
		}
		Map<String, List<String>> fieldMap = new HashMap<String, List<String>>();
		for (int i = 0; i < allPo.length; i++) {
			String[] arrayIgnoreFields = getArrayIgnoreFields(ignoreField,
					allPo[i]);
			Field[] poFields = allPo[i].getDeclaredFields();
			List<String> fields = new ArrayList<String>();
			for (Field field : poFields) {
				String fieldName = field.getName();
				if (isIgnore(arrayIgnoreFields, fieldName)) {
					continue;
				}
				try {
					voClass.getDeclaredField(fieldName);
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					continue;
				}
				fields.add(fieldName);
			}
			fieldMap.put(allPo[i].getSimpleName(), fields);
		}
		voResolve.setFieldMap(fieldMap);
		return voResolve;
	}

	private String[] getArrayIgnoreFields(Map<Class<?>, String> ignoreField,
			Class<?> entityClass) {
		String ignoreFields = ignoreField.get(entityClass);
		if (ignoreFields != null) {
			return ignoreFields.split(",");
		}
		return null;
	}

	private boolean isIgnore(String[] arrayIgnoreFields, String fieldName) {
		if (arrayIgnoreFields != null) {
			for (String arrayIgnoreField : arrayIgnoreFields) {
				if (arrayIgnoreField.equalsIgnoreCase(fieldName)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 将vo对象的值转换到对应的po对象中
	 * 
	 * @param voResolve
	 * @param entity
	 */
	public void convertVoToPo(VoResolve voResolve, Object entity) {
		Class<?>[] needPoObjectClass = voResolve.getNeedPoObjectClass();
		Object[] poObject = new Object[needPoObjectClass.length];
		for (int i = 0; i < needPoObjectClass.length; i++) {
			try {
				poObject[i] = needPoObjectClass[i].newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			List<String> fields = voResolve.getFieldMap().get(
					needPoObjectClass[i].getSimpleName());
			for (String field : fields) {
				try {
					Object retVal = PropertyUtils.getProperty(entity, field);
					PropertyUtils.setProperty(poObject[i], field, retVal);
					// 以下异常不会产生
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		voResolve.setPoObject(poObject);
	}

	public String getVoSql(VoResolve voResolve) {
		String sql = null;
		StringBuilder sqlBuilder = new StringBuilder("select ");
		// 拼装sql语句
		// 用于存储主键名和表名
		Map<String[], String> primaryAndTableNames = new HashMap<String[], String>();
		/*
		 * // 用于存储表名 List<String> tableNames = new ArrayList<String>(); //
		 * 　用于存储主键名 List<String[]> primaryKeys = new ArrayList<String[]>(); //
		 * 　用于存储外键名 Map<String, String> foreignAndTableNames = new
		 * HashMap<String, String>();
		 */
		// 用于存储外键名
		Map<String[], String> foreignAndTableNames = new HashMap<String[], String>();
		// 　用于存储外键名
		// List<String[]> foreignKeys = new ArrayList<String[]>();

		Map<String, List<String>> fieldMap = voResolve.getFieldMap();
		Class<?>[] allPo = voResolve.getAllPo();
		AnnotationUtil au = AnnotationUtil.getInstance();
		for (int i = 0; i < allPo.length; i++) {
			String tableName = au.getAnnotationTableName(allPo[i]);
			// 将表名，主键，外键加入到容器中
			primaryAndTableNames.put(au.getPrimaryKey(allPo[i]), tableName);
			foreignAndTableNames.put(au.getForeignKey(allPo[i]), tableName);
			/*
			 * tableNames.add(tableName);
			 * primaryKeys.add(au.getPrimaryKey(allPo[i]));
			 */
			// foreignKeys.add(au.getForeignKey(allPo[i]));
			List<String> fields = fieldMap.get(allPo[i].getSimpleName());
			for (String field : fields) {
				// 当此字段不存在时插入
				if (sqlBuilder.indexOf(field) == -1) {
					sqlBuilder.append(tableName + "." + field + ",");
				}
			}
		}
		sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
		sqlBuilder.append(" from ");

		Collection<String> co = primaryAndTableNames.values();
		// 添加表名到from语句之后
		for (Iterator<String> it = co.iterator(); it.hasNext();) {
			sqlBuilder.append(it.next() + ",");
		}
		// 构建where之后的语句
		Set<String[]> foreignSet = foreignAndTableNames.keySet();
		Set<String[]> primarySet = primaryAndTableNames.keySet();
		StringBuilder conditionBuilder = new StringBuilder();
		for (Iterator<String[]> it = primarySet.iterator(); it.hasNext();) {
			String[] primaryNames = it.next();
			// 当主键只有一个时进行组合
			if (primaryNames.length == 1) {
				String primaryName = primaryNames[0];
				if (conditionBuilder.indexOf(primaryName) != -1) {
					continue;
				}
				for (Iterator<String[]> it1 = foreignSet.iterator(); it1
						.hasNext();) {
					String foreignNames[] = it1.next();
					for (String foreignName : foreignNames) {
						if (foreignName.indexOf(primaryName) != -1) {

							conditionBuilder.append(primaryAndTableNames
									.get(primaryNames)
									+ "."
									+ primaryName
									+ "="
									+ foreignAndTableNames.get(foreignNames)
									+ "." + foreignName + " and ");
						}
					}
				}
			}
		}
		sqlBuilder.deleteCharAt(sqlBuilder.lastIndexOf(","));
		if (conditionBuilder.lastIndexOf("and") != -1) {
			conditionBuilder.delete(conditionBuilder.lastIndexOf("and"),
					conditionBuilder.length());
			sqlBuilder.append(" where ");
		}
		// 得到完整的sql语句
		sql = sqlBuilder.toString() + conditionBuilder.toString();
		return sql;
	}

}
