package org.web.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.web.access.factory.OperateServiceExecuteAdviceFactory;
import org.web.exception.BeanInitializationException;
import org.web.exception.ErrorException;
import org.web.framework.action.AutoWire;
import org.web.service.OperateServiceExecuteAdvice;
import org.web.servlet.ActionSupport;
import org.web.servlet.HttpServletRequestAware;

public class OperateAction extends ActionSupport implements
		HttpServletRequestAware {

	private HttpServletRequest request;

	private String viewName ;
	
	private String operate;
	
	@AutoWire
	private List<Object> list;

	@Override
	public String execute() throws Exception{
		viewName = this.action.substring(0, this.action.lastIndexOf("_"));
		operate = this.action.substring(this.action.lastIndexOf("_") + 1);
		if(operate.equalsIgnoreCase("import")) {
			request.getParameter("uploadFile");
		}
		try {
			this.executeOperate(list, viewName, operate);
		} catch (ErrorException e) {
			//e.printStackTrace();
			this.addMessage(e.getMessage());
		/*	this.allowSendReuqstParam();
			this.setProperties("u_id", request.getParameter("u_id"));*/
			return ERROR;
		}
		return SUCCESS;
	}

	public void executeOperate(List<Object> list, String viewName,
			String operate) throws ErrorException, BeanInitializationException {
		System.out.println(list);
		OperateServiceExecuteAdvice service = OperateServiceExecuteAdviceFactory
				.getService(viewName);
		service.execute(list, operate);
	}

	@Override
	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}

}
