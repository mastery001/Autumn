package org.web.servlet.adapter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认是代理HttpServletRequest的方法的服务
 * @author Administrator
 *
 */
class DefaultProxyRequestAdvice extends AbstractProxyRequestAdvice{

	public DefaultProxyRequestAdvice(HttpServletRequest reqeust){
		super(reqeust);
	}
	
	@Override
	public String getParameter(String name) {
		return this.request.getParameter(name);
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return this.request.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.request.getParameterValues(name);
	}
	
	
	
}
