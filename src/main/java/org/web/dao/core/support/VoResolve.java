package org.web.dao.core.support;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class VoResolve {

	private Class<?> voClass;

	private Class<?>[] allPo;

	private Map<String, List<String>> fieldMap;

	// 忽略的字段，如果有多个则以逗号隔开
	private Map<Class<?>, String> ignoreField;

	private Object[] poObject;

	/**
	 * 需要获得的po对象
	 */
	private Class<?>[] needPoObjectClass;

	public Class<?> getVoClass() {
		return voClass;
	}

	public void setVoClass(Class<?> voClass) {
		this.voClass = voClass;
	}

	public Class<?>[] getAllPo() {
		return allPo;
	}

	public void setAllPo(Class<?>[] allPo) {
		this.allPo = allPo;
	}

	public Map<String, List<String>> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, List<String>> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public Map<Class<?>, String> getIgnoreField() {
		return ignoreField;
	}

	public void setIgnoreField(Map<Class<?>, String> ignoreField) {
		this.ignoreField = ignoreField;
	}

	public Object[] getPoObject() {
		return poObject;
	}

	public void setPoObject(Object[] poObject) {
		this.poObject = poObject;
	}

	public Class<?>[] getNeedPoObjectClass() {
		if (needPoObjectClass == null) {
			needPoObjectClass = new Class<?>[allPo.length];
			for (int i = 0; i < needPoObjectClass.length; i++) {
				needPoObjectClass[i] = allPo[i];
			}
		}
		return needPoObjectClass;
	}

	public void setNeedPoObjectClass(Class<?>[] needPoObjectClass) {
		this.needPoObjectClass = needPoObjectClass;
	}

	@Override
	public String toString() {
		return "VoResolve [voClass=" + voClass + ", allPo="
				+ Arrays.toString(allPo) + ", fieldMap=" + fieldMap
				+ ", ignoreField=" + ignoreField + ", poObject="
				+ Arrays.toString(poObject) + ", needPoObjectClass="
				+ Arrays.toString(needPoObjectClass) + "]";
	}

}
