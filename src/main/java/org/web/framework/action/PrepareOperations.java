package org.web.framework.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.action.support.ActionMapping;

public class PrepareOperations {

	private ServletContext servletContext;

	private Dispatcher dispatcher;

	public PrepareOperations(ServletContext servletContext,
			Dispatcher dispatcher) {
		super();
		this.servletContext = servletContext;
		this.dispatcher = dispatcher;
	}

	/**
	 * 找到Action的映射对象
	 * @param request
	 * @param response
	 * @param actionName
	 * @return
	 * @throws ServletException 
	 */
	public ActionMapping findActionMapping(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException  {
		ActionMapping mapping = this.dispatcher.getConfiguration()
				.getActionMapper().getMapping(request , response);
		return mapping;
	}
	
	/**
	 * 封装request对象
	 * @param request
	 * @return
	 * @throws ServletException
	 */
	public HttpServletRequest wrapRequest(HttpServletRequest request) throws ServletException {
		try {
			return this.dispatcher.wrapRequest(request, servletContext);
		} catch (IOException e) {
			 throw new ServletException("Could not wrap servlet request with MultipartRequestWrapper!", e);
		}
	}
}
