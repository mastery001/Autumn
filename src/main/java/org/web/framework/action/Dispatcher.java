package org.web.framework.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.ConfigLibrary;
import org.web.framework.action.support.ActionMapping;
import org.web.framework.action.support.InjectAttribute;
import org.web.framework.action.support.XmlConfigurationProvider;
import org.web.servlet.adapter.GlobalRequestAdapter;

public class Dispatcher {

	@SuppressWarnings("unused")
	private ServletContext servletContext;

	private ActionConfiguration configuration;

	public Dispatcher(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public void init() {
		if (configuration == null) {
			configuration = new ActionConfiguration();
		}
		init_TraditionalXmlConfigurations();
		init_Constant();
	}

	private void init_Constant() {
		new InjectAttribute().init();
	}

	private void init_TraditionalXmlConfigurations() {
		String configPaths = ConfigLibrary.ACTIONXMLPATH;

		this.configuration
				.setConfigurationProvider(createXmlConfigurationProvider(configPaths));
	}

	private ConfigurationProvider createXmlConfigurationProvider(String filename) {
		XmlConfigurationProvider xcp = new XmlConfigurationProvider(filename);
		xcp.register();
		return xcp;
	}

	/**
	 * 封装request对象
	 * 
	 * @param request
	 * @param servletContext
	 * @return
	 * @throws IOException
	 */
	public HttpServletRequest wrapRequest(HttpServletRequest request,
			ServletContext servletContext) throws IOException {
		return GlobalRequestAdapter.getInstance().parseRequest(request,
				this.configuration.getUploadFileAtttibute());
	}

	/**
	 * 调用action
	 * 
	 * @param request
	 * @param response
	 * @param servletContext
	 * @param mapping
	 * @throws ServletException
	 */
	public void serviceAction(HttpServletRequest request,
			HttpServletResponse response, ServletContext servletContext,
			ActionMapping mapping) throws ServletException {
		String name = mapping.getActionName();
		ActionInvocation paramActionInvocation = this.getConfiguration()
				.createActionInvocation(name, request, response);
		if (mapping.getResult() != null) {
			Result result = mapping.getResult();
			try {
				result.execute(paramActionInvocation);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}
	}

	public ActionConfiguration getConfiguration() {
		return configuration;
	}

}
