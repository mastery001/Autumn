package org.web.framework.action.config;



/**
 * Action的属性的配置文件
 * @author mastery
 * @Time 2015-3-30 下午3:50:31
 * 
 */
public class ActionConfig{
	
	// action name
	private String name;
	
	// action's className
	private String className;
	
	private Integer match;
	
	// 验证令牌
	private boolean validate_token;
	
	private String method;
	
	private String[] splitAction;
	
	public ActionConfig() {
		super();
	}

	public ActionConfig(String name, String className) {
		super();
		this.name = name;
		this.className = className;
	}

	public ActionConfig(String name, String className, Integer match) {
		super();
		this.name = name;
		this.className = className;
		this.match = match;
	}

	public ActionConfig(String name, String className,String method) {
		super();
		this.name = name;
		this.className = className;
		this.method = method;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getMatch() {
		return match;
	}

	public void setMatch(Integer match) {
		this.match = match;
	}

	public boolean isValidate_token() {
		return validate_token;
	}

	public void setValidate_token(boolean validate_token) {
		this.validate_token = validate_token;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String[] getSplitAction() {
		return splitAction;
	}

	public void setSplitAction(String[] splitAction) {
		this.splitAction = splitAction;
	}

	@Override
	public String toString() {
		return "ActionConfig [name=" + name + ", className=" + className
				+ ", match=" + match + ", validate_token=" + validate_token
				+ ", method=" + method + "]";
	}

}
