package org.web.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.web.dao.core.support.Page;
import org.web.exception.ActionExecuteException;
import org.web.exception.BeanInitializationException;
import org.web.exception.ErrorException;
import org.web.framework.action.AutoWire;
import org.web.service.QueryService;
import org.web.servlet.ActionSupport;
import org.web.servlet.FilePathAware;
import org.web.servlet.HttpServletRequestAware;

import tool.mastery.log.Logger;

public class QueryAction extends ActionSupport implements
		HttpServletRequestAware , FilePathAware{

	private static final Logger LOG = Logger.getLogger(QueryAction.class);
	
	private HttpServletRequest request;
	
	private String viewName;
	
	@SuppressWarnings("unused")
	private Map<String , String> filePath;

	@AutoWire
	private Object vo;
	
	@Override
	public String execute() throws ActionExecuteException {
		viewName = this.action.substring(0, this.action.lastIndexOf("."));
		try {
			this.executeQuery(viewName, vo);
		} catch (ErrorException e) {
			// e.printStackTrace();
			this.addMessage(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	public void executeQuery(String viewName, Object bean)
			throws ErrorException, BeanInitializationException {
		LOG.debug("bean is " + bean);
		QueryService service = new QueryService(viewName);
		List<Object> list = service.getResult(bean, getPage());
		LOG.debug("list is : " + list.size());
		this.request.setAttribute("list", list);
	}

	private Page getPage() {
		Page page = new Page();
		// 获得从页面中传递过来的数据
		String firstIndex = this.request.getParameter("firstIndex");
		if (firstIndex != null) {
			int temp = Integer.parseInt(firstIndex);
			page.setPage(temp);
		}
		return page;
	}


	@Override
	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public void setFilePath(Map<String , String> filePath) {
		this.filePath = filePath;
	}

}
