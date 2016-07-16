package org.web.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.action.Dispatcher;
import org.web.framework.action.ExecuteOperations;
import org.web.framework.action.PrepareOperations;
import org.web.framework.action.support.ActionMapping;
import org.web.framework.action.support.InitOperations;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {

	protected PrepareOperations prepare;
	protected ExecuteOperations execute;

	@Override
	public void init() throws ServletException {

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		InitOperations init = new InitOperations(config);
		
		Dispatcher dispatcher = init.initDispatcher();
		this.prepare = new PrepareOperations(config.getServletContext(),
				dispatcher);
		this.execute = new ExecuteOperations(config.getServletContext(),
				dispatcher);
		try {
			init.init();
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionMapping mapping = this.prepare.findActionMapping(request,
				response);
		if(mapping == null) {
			return ;
		}
		request = this.prepare.wrapRequest(request);
		this.execute.executeAction(request, response, mapping);
	}

}
