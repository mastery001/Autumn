package org.web.servlet;


public interface Action {
	
	public static final String SUCCESS = "success";
	
	public static final String ERROR = "error";
	
	public static final String DEFAULT = "default";
	
	public static final String INPUT = "input";
	
	public static final String NONE = "none";
	
	/**
	 * 执行action
	 * @return	
	 */
	public String execute() throws Exception;
}
