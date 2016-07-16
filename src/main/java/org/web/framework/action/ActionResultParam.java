package org.web.framework.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * 当前action对应request，且需要传递参数值给xml,或可以返回错误信息
 * @author mastery
 * @Time 2015-4-11 上午11:29:00
 * 
 */
public class ActionResultParam {
	
	private Map<String , String[]> params = new HashMap<String , String[]>(); 
	
	private HttpServletRequest requestWrapper;

	// 设置在requestWrapper过滤中不需要清楚的参数
	private String[] ignoreParams;
	
	private List<String> info;
	
	public ActionResultParam() {
		info = new ArrayList<String>();
	}
	
	public void addMessage(String responseMessage) {
		info.add(responseMessage);
	}

	public List<String> getMessage() {
		return info;
	}
	
	public void setProperties(String key , String value) {
		params.put(key, new String[]{value});
	}
	
	public String[] getProperty(String key) {
		return this.params.get(key);
	}
	
	public void clearProperties() {
		params = new HashMap<String , String[]>();
	}
	
	public HttpServletRequest getRequestWrapper() {
		return requestWrapper;
	}

	public void setRequestWrapper(HttpServletRequest requestWrapper) {
		this.requestWrapper = requestWrapper;
	}

	public String toParamLayout() {
		if(params.size() != 0) {
			StringBuilder sBuilder = new StringBuilder("?");
			Set<String> set = params.keySet();
			for(Iterator<String> it = set.iterator() ; it.hasNext() ; ) {
				String key = it.next();
				sBuilder.append(key + "=" + params.get(key)[0] + "&");
			}
			sBuilder.deleteCharAt(sBuilder.lastIndexOf("&"));
			return sBuilder.toString();
		}
		return "";
	}

	public String[] getIgnoreParams() {
		return ignoreParams;
	}

	public void setIgnoreParams(String[] ignoreParams) {
		this.ignoreParams = ignoreParams;
	}

	public Map<String, String[]> getPropertys() {
		return this.params;
	}
}
