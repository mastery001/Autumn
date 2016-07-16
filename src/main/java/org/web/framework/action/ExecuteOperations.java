package org.web.framework.action;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.action.support.ActionMapping;

public class ExecuteOperations {

	private ServletContext servletContext;
	private Dispatcher dispatcher;

	public ExecuteOperations(ServletContext servletContext,
			Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.servletContext = servletContext;
	}

	public void executeAction(HttpServletRequest request,
			HttpServletResponse response, ActionMapping mapping)
			throws ServletException {
		this.dispatcher.serviceAction(request, response, this.servletContext,
				mapping);
	}
}
