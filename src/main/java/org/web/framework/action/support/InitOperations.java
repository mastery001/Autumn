package org.web.framework.action.support;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.web.framework.Configuration;
import org.web.framework.action.Dispatcher;
import org.web.framework.service.CacheMaster;
import org.web.framework.support.XmlBeanFactory;

import tool.mastery.core.ClassUtil;
import tool.mastery.core.StringUtil;

public class InitOperations {

	public static String ServicesPath = null;

	private ServletConfig config;

	public InitOperations(ServletConfig config) {
		this.config = config;
	}

	public void init() throws Exception {
		initConfiguration();
		initServicesPath();
	}

	/**
	 * 初始化服务器路径
	 */
	private void initServicesPath() {
		if (ServicesPath == null) {
			ServicesPath = this.config.getServletContext().getRealPath("/");
		}
	}

	public Dispatcher initDispatcher() {
		Dispatcher d = new Dispatcher(config.getServletContext());
		d.init();
		return d;
	}

	/**
	 * 初始化配置
	 * 
	 * @throws Exception
	 */
	private void initConfiguration() throws Exception {
		String cacheClassName = config.getInitParameter("cache");
		if(!StringUtil.StringIsNull(cacheClassName)) {
			Object bean = ClassUtil.getObjectByName(cacheClassName);
			if (bean instanceof CacheMaster) {
				Configuration.setCacheMaster((CacheMaster) bean);
			} else {
				throw new ServletException("配置的类不是CacheMaster的实现类！请重新配置！");
			}
		}
		Configuration.setBeanFactory(new XmlBeanFactory());
	}
}
