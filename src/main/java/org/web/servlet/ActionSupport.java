package org.web.servlet;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.web.framework.action.ActionResultParam;

public abstract class ActionSupport implements Action {

	/**
	 * 用户的请求
	 */
	protected String action;

	/**
	 * 是否允许设置到移除request中的参数
	 * 
	 * @return
	 */
	private boolean removeParam;

	private final ActionResultParam actionResultParam = new ActionResultParam();

	public void setAction(String action) {
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public List<String> getResponseMessage() {
		return actionResultParam.getMessage();
	}

	/**
	 * 设置servlet的返回信息
	 * 
	 * @param responseMessage
	 */
	@Deprecated
	public void setResponseMessage(String responseMessage) {
		actionResultParam.addMessage(responseMessage);
	}

	/**
	 * 设置servlet的返回信息
	 * 
	 * @param responseMessage
	 */
	public void addMessage(String responseMessage) {
		actionResultParam.addMessage(responseMessage);
	}

	/**
	 * 获取action后?和后面的参数值
	 * 
	 * @return
	 */
	public String getActionParameter() {
		return actionResultParam.toParamLayout();
	}

	protected void setProperties(String key, String value) {
		actionResultParam.setProperties(key, value);
	}

	protected void clearProperties() {
		actionResultParam.clearProperties();
	}

	protected void allowRemoveRequestParam() {
		this.removeParam = true;
	}
	
	public boolean isRemoveParam() {
		return removeParam;
	}

	protected void setRequestWrapper(HttpServletRequest requestWrapper) {
		if(removeParam) {
			actionResultParam.setRequestWrapper(requestWrapper);
		}
	}

	protected void setIgnoreRemoveRequestParam(String[] ignoreParams) {
		if(removeParam) {
			actionResultParam.setIgnoreParams(ignoreParams);
		}
	}
	
	public String[] getIgnoreParams() {
		return actionResultParam.getIgnoreParams();
	}
	
	public HttpServletRequest getRequestWrapper() {
		return actionResultParam.getRequestWrapper();
	}

	public Map<String , String[]> getProperties() {
		return actionResultParam.getPropertys();
	}
	
}
