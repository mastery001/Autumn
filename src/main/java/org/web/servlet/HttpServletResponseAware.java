package org.web.servlet;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletResponseAware {
	
	void setHttpServletResponse(HttpServletResponse response);
}
