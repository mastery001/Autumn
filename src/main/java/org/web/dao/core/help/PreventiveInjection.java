package org.web.dao.core.help;


/**
 * 防范sql注入
 * @author mastery
 * @Time 2015-4-29 下午12:37:24
 * 
 */
public class PreventiveInjection {
	
	public static final PreventiveInjection INSTANCE = new PreventiveInjection();
	
	public static PreventiveInjection getInstance() {
		return INSTANCE;
	}
	
	private String[] interceptPatterns;
	
	private PreventiveInjection() {
		interceptPatterns = new String[]{".*'or'.*'='.*" , ".*select.*"};
	}
	
	public Object process(String value){
		for(String interceptPattern : interceptPatterns) {
			if(value.toLowerCase().matches(interceptPattern)) {
				value = "1'and'1'='2";
				return value;
			}
		}
		return value;
	}
}
