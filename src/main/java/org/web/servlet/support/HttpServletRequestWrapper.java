package org.web.servlet.support;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 移除不需要的request的参数
 * 
 * @author mastery
 * @Time 2014-11-21 上午11:36:25
 * 
 */
public class HttpServletRequestWrapper extends
		javax.servlet.http.HttpServletRequestWrapper {
	protected Map<String, String[]> params;

	protected HttpServletRequest request;

	private String[] ignoreParams;

	public HttpServletRequestWrapper(HttpServletRequest originalRequest,
			String[] ignoreParams) {
		super(originalRequest);
		this.request = originalRequest;
		this.ignoreParams = ignoreParams;
		
		params = originalRequest.getParameterMap();
		Method method = null;
		try {
			method = params.getClass().getDeclaredMethod("setLocked",
					boolean.class);
			if (method != null) {
				method.invoke(params, false);
			}
		} catch (Exception e) {
			throw new AssertionError(e);
		}
		Enumeration<String> en = originalRequest.getParameterNames();
		while (en.hasMoreElements()) {
			String paramName = en.nextElement().toString();
			if (isIgnore(paramName)) {
				String[] paramValue = params.get(paramName);
				if (paramValue.length != 1) {
					params.put(paramName, new String[] { paramValue[1] });
				}
				continue;
			}
			params.remove(paramName);
		}
	}

	private boolean isIgnore(String paramName) {
		if (ignoreParams == null || ignoreParams.length == 0) {
			return false;
		}
		for (String ignoreParam : ignoreParams) {
			if (ignoreParam.equals(paramName)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, String[]> getParameterMap() {
		return params;
	}

	public String getParameter(String name) {
		if (params.get(name) != null) {
			return params.get(name)[0];
		}
		return null;
	}

	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(params.keySet());
	}

	public String[] getParameterValues(String name) {
		return params.get(name);
	}

	@Override
	public HttpSession getSession() {
		return request.getSession();
	}

	public void setParams(Map<String, String[]> params) {
		this.params.putAll(params);
	}
	
}
