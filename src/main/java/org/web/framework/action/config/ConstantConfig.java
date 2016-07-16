package org.web.framework.action.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 常量全局配置
 * @author mastery
 * @Time 2015-4-17 下午1:09:11
 * 
 */
public class ConstantConfig {
	
	private static final ConstantConfig INSTANCE = new ConstantConfig();
	
	private final Map<String , String> constantMap = new HashMap<String , String>();
	
	private ConstantConfig() {}
	
	public static ConstantConfig getInstance() {
		return INSTANCE;
	}
	
	public void put(String key , String value){
		constantMap.put(key, value);
	}
	
	public String get(String key) {
		return constantMap.get(key);
	}
	
	public Set<String> keySet() {
		return constantMap.keySet();
	}
	
	public boolean constains(String key) {
		return constantMap.containsKey(key);
	}
}
