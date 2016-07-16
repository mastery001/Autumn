package org.web.framework.action.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.action.ActionConfiguration;
import org.web.framework.action.ActionMapper;

/**
 * action的映射者
 * 
 * @author mastery
 * @Time 2015-4-11 下午8:58:32
 * 
 */
public class DefaultActionMapper implements ActionMapper {

	private ActionConfiguration ac;

	public DefaultActionMapper(ActionConfiguration ac) {
		super();
		this.ac = ac;
	}

	@Override
	public ActionMapping getMapping(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = ActionHelper
				.processRequestPath(request.getRequestURI());
		// 判断是否需要验证令牌
		if(ac.getConfigurationProvider().getConfig(action).getActionConfig()
				.isValidate_token()) {
			boolean flag = TokenInterceptor.getInstance().validateToken(action,
					request, response);
			if (flag) {
				return null;
			}
		}
		ActionMapping am = new ActionMapping();
		am.setActionName(action);
		am.setResult(new ActionResult(ac, request, response));
		return am;
	}

}
