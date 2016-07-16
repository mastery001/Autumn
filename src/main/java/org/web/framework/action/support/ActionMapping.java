package org.web.framework.action.support;

import org.web.framework.action.Result;


/**
 * action对应的映射
 * @author mastery
 * @Time 2015-4-11 下午8:57:39
 * 
 */
public class ActionMapping {
	
	// action name
	private String actionName;
	
	// 执行结果
	private Result result;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ActionMapping [actionName=" + actionName + ", result=" + result
				+ "]";
	}
	
}
