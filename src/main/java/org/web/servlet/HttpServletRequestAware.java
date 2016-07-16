package org.web.servlet;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestAware {
	
	void setHttpServletRequest(HttpServletRequest request);
	
}
