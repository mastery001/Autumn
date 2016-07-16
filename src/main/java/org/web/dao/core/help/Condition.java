package org.web.dao.core.help;

import java.lang.reflect.Field;
import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.web.dao.core.SqlConstant;

import tool.mastery.log.Logger;

public class Condition {

	public static final Logger LOG = Logger.getLogger(Condition.class);
	
	private String condition;
	private String value;

	public Condition() {
	}

	public Condition(Object vo) {
		init(vo);
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Condition [condition=" + condition + ", value=" + value + "]";
	}

	/**
	 * 将对象转换成值
	 * 
	 * @param vo
	 */
	@SuppressWarnings("deprecation")
	private void init(Object vo) {
		if (vo == null) {
			return;
		}
		Field[] fields = vo.getClass().getDeclaredFields();
		StringBuilder conditionBuilder = new StringBuilder();
		StringBuilder valueBuilder = new StringBuilder();
		for (Field field : fields) {
			String fieldName = field.getName();
			// 获取该属性的值
			Object fieldValue = null;
			try {
				fieldValue = PropertyUtils.getProperty(vo, fieldName);
				if (fieldValue == null) {
					continue;
				}
				/*fieldValue = PreventiveInjection.getInstance().process(
						fieldValue.toString());*/
				if (PropertyUtils.getPropertyType(vo, fieldName)
						.getSimpleName().toLowerCase().indexOf("date") != -1) {
					fieldValue = ((Date) fieldValue).toLocaleString();
				}
			} catch (Exception e) {
				LOG.debug(e.getMessage() , e);
				continue;
			}
			if (fieldValue != null && !fieldValue.toString().trim().equals("")) {
				conditionBuilder
						.append(fieldName + SqlConstant.CONDITION_SPLIT);
				valueBuilder.append(fieldValue + SqlConstant.CONDITION_SPLIT);
			}
		}

		if (conditionBuilder.length() != 0) {
			// 将多余的and删除
			condition = conditionBuilder.delete(
					conditionBuilder.lastIndexOf(SqlConstant.CONDITION_SPLIT),
					conditionBuilder.length()).toString();
			value = valueBuilder.delete(
					valueBuilder.lastIndexOf(SqlConstant.CONDITION_SPLIT),
					valueBuilder.length()).toString();
		}
	}

}
