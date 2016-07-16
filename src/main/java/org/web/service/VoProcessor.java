package org.web.service;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.web.exception.VoProcessorException;

import tool.mastery.log.Logger;

/**
 * 负责对vo或者po对象的处理类
 * @author mastery
 * @Time 2015-3-8 下午11:16:12
 * 
 */
public abstract class VoProcessor implements ObjectAdapter{

	public static final Logger LOG = Logger.getLogger(VoProcessor.class);
	
	protected List<Object> list;
	
	private boolean isReverse = true;
	
	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	/**
	 * 将对应的视图vo转换的操作vo，或者是逆转换,默认是正向转换
	 * @param voClass	转换目标类的字节码
	 * @param vos		需转换成的类
	 * @return
	 * @throws VoProcessorException
	 */
	public List<Object> getVo(List<Object> vos)
			throws VoProcessorException {
		list = vos;
		// 判断传递进来的vos是否为空或者长度为0，如是则返回一个长度为0的list
		if (vosIsEpmty(vos)) {
			return list;
		}
		
		// 当是逆转换时调用逆转换方法.
		if(isReverse) {
			list = reverseConvert(vos);
		}else {
			list = convert(vos);
		}
		if(list == null) {
			return vos;
		}
		return list;
	}
	
	
	/**
	 * 正向转换,从前台至数据库
	 * @param voClass
	 * @param vos
	 * @return
	 * @throws VoProcessorException
	 */
	protected abstract List<Object> convert(List<Object> vos)
			throws VoProcessorException;
	
	/**
	 * 逆向转换,,从前台至数据库
	 * @param voClass
	 * @param vos
	 * @return
	 * @throws VoProcessorException
	 */
	protected abstract List<Object> reverseConvert(List<Object> vos)
			throws VoProcessorException;
	
	/**
	 * 是否是逆向转换,此方法已经过期，而且此方法不会被调用
	 * @param voClass
	 * @return
	 */
	@Deprecated
	protected boolean isReverse(Object vo ){
		return false;
	}
	
	/**
	 * 将一个对象属性值赋值到另一个对象中
	 * @param retObj		需要赋值的对象
	 * @param obj			有值的对象
	 * @param uncopyField	不需要赋值的字段
	 * @return
	 * @throws VoProcessorException 
	 */
	protected void copyValue(Object retObj , Object obj , String uncopyField) throws VoProcessorException {
		//得到值对象的所有字段
		Field[] objFields = obj.getClass().getDeclaredFields();
		if(uncopyField == null) {
			uncopyField = "";
		}
		//得到不需要赋值的字段
		String[] uncopyFields = uncopyField.split(",");
		for(Field field : objFields) {
			String objFieldName = field.getName();
			
			//当两者中的属性字段名不相同时
			try {
				retObj.getClass().getDeclaredField(objFieldName);
			} catch (Exception e1) {
				continue;
			} 
			if(!isExist(objFieldName, uncopyFields)) {
				try {
					//得到该字段的值
					Object retVal = PropertyUtils.getProperty(obj, objFieldName);
					//将该字段的值赋给需要返回的对象
					PropertyUtils.setProperty(retObj, objFieldName, retVal);
				} catch (Exception e) {
					//e.printStackTrace();
					throw new VoProcessorException("值转换错误！请检查vo对象中的属性名和操作vo对象的属性名是否匹配!");
				} 
			}
		}
	}
	
	private boolean isExist(String fieldName , String[] uncopyFields) {
		for(int i = 0 ; i < uncopyFields.length ; i ++) {
			if(fieldName.equals(uncopyFields[i])) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断传递进来的vo的list集合是否为空或者长度为0
	 * @param vos
	 * @return
	 */
	private <E> boolean vosIsEpmty(List<E> vos) {
		if (vos == null) {
			return true;
		}
		if (vos.size() == 0) {
			return true;
		}
		return false;
	}
}
