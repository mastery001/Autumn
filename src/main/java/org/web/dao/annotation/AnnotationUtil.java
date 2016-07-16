package org.web.dao.annotation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;


public class AnnotationUtil {

	public static final AnnotationUtil ANNOTAION_UTIL = new AnnotationUtil();
	
	
	public static AnnotationUtil getInstance() {
		return ANNOTAION_UTIL;
	}
	
	
	private AnnotationUtil() {
	}
	
	/**
	 * 通过传递的实体类获得类上的注解过的表名
	 * @param entityClass
	 * @return
	 */
	public String getAnnotationTableName(Class<?> entityClass) {
		String tableName = null;
		if(entityClass.isAnnotationPresent(TableAnnotation.class)) {
			TableAnnotation tableAnnotation = entityClass.getAnnotation(TableAnnotation.class);
			tableName = tableAnnotation.value();
		}
		return tableName;
	}
	
	/**
	 * 通过传递的实体类获得方法上的注解过的主键名
	 * @param entityClass
	 * @return
	 */
	public String[] getPrimaryKey(Class<?> entityClass) {
		List<String> primaryKeys = new ArrayList<String>();
		Method[] methods = entityClass.getMethods();
		for(Method method : methods) {
			if(method.isAnnotationPresent(PrimaryKeyAnnotation.class)) {
				PrimaryKeyAnnotation primaryKeyAnnotation = method.getAnnotation(PrimaryKeyAnnotation.class);
				String primaryKey = primaryKeyAnnotation.id();
				primaryKeys.add(primaryKey);
			}
		}
		return primaryKeys.toArray(new String[]{});
	}
	
	/**
	 * 通过传递的实体类获得方法上的注解过的外键名
	 * @param entityClass
	 * @return
	 */
	public String[] getForeignKey(Class<?> entityClass) {
		List<String> foreignKeys = new ArrayList<String>();
		Method[] methods = entityClass.getMethods();
		for(Method method : methods) {
			if(method.isAnnotationPresent(ForeignKeyAnnotation.class)) {
				ForeignKeyAnnotation foreignKeyAnnotation = method.getAnnotation(ForeignKeyAnnotation.class);
				String foreignKey = foreignKeyAnnotation.value();
				foreignKeys.add(foreignKey);
			}
		}
		return foreignKeys.toArray(new String[]{});
	}
	
	/**
	 * 通过传递的bean对象的得到对应的map集合
	 * 集合中存储了此对象中所有的字段和对应的值 
	 * @param bean
	 * @return
	 */
	public Map<String , Object> getBeanInfo(Object bean) {
		Map<String , Object> beanMap = new HashMap<String , Object>();
		
		try {
			//获得当前bean对象的所有映射信息
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			//获得bean中的所有描述
			PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
			for(PropertyDescriptor pd : pds) {
				//获得get的方法
				Method readMethod = pd.getReadMethod();
				//获得字段名
				String fieldName = pd.getName().toLowerCase();
				Object retVal = readMethod.invoke(bean);
				if(retVal != null) {
					beanMap.put(fieldName, retVal);
				}
 			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return beanMap;
	}
	
	/**
	 * 通过传递一个bean对象的字节码，返回一个存储了字段名和对其的所有描述的map集合
	 * @param entityClass
	 * @return
	 * @throws IntrospectionException
	 */
	public Map<String , PropertyDescriptor> getBeanInfo(Class<?> entityClass) throws IntrospectionException {
		Map<String , PropertyDescriptor> beanMap = new HashMap<String , PropertyDescriptor>();
		BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
		PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor pd : pds) {
			String fieldName = pd.getName().toLowerCase();
			beanMap.put(fieldName, pd);
		}
		return beanMap;
	}
	
	/**
	 * 获得所有主键的值
	 * @param entityClass
	 * @param entity
	 * @return
	 */
	public Map<String , Object> getPrimaryValue(Object entity) {
		Map<String , Object> primary = new HashMap<String , Object>();
		String[] primaryName = this.getPrimaryKey(entity.getClass());
		for(int i = 0 ; i < primaryName.length ; i ++) {
			try {
				Object retVal = PropertyUtils.getProperty(entity, primaryName[i]);
				primary.put(primaryName[i], retVal);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return primary;
	}
	
}	
