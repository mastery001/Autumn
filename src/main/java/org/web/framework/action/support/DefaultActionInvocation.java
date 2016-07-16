package org.web.framework.action.support;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.exception.ActionExecuteException;
import org.web.framework.ConfigLibrary;
import org.web.framework.action.ActionConfiguration;
import org.web.framework.action.ActionInvocation;
import org.web.framework.action.AutoWire;
import org.web.framework.action.config.ActionConfig;
import org.web.framework.action.config.Config;
import org.web.servlet.ActionSupport;
import org.web.servlet.FetchFormValueAdvice;
import org.web.servlet.FilePathAware;
import org.web.servlet.HttpServletRequestAware;
import org.web.servlet.HttpServletResponseAware;
import org.web.servlet.support.DefaultFetchFormValueAdvice;

import tool.mastery.core.ClassUtil;

/**
 * 默认的action调用者
 * 
 * @author mastery
 * @Time 2015-4-11 下午8:58:05
 * 
 */
public class DefaultActionInvocation implements ActionInvocation {

	private ActionConfiguration actionConfiguration;

	// action的名称
	private String name;

	private HttpServletRequest request;

	private HttpServletResponse response;

	public DefaultActionInvocation(ActionConfiguration actionConfiguration,
			String name, HttpServletRequest request,
			HttpServletResponse response) {
		super();
		this.actionConfiguration = actionConfiguration;
		this.name = name;
		this.request = request;
		this.response = response;
	}

	@Override
	public Object getAction() throws ActionExecuteException {
		Config config = this.actionConfiguration.getConfigurationProvider()
				.getConfig(name);
		if (config == null) {
			throw new ActionExecuteException(name + "对应的action尚未在action.xml配置！");
		}
		ActionConfig ac = config.getActionConfig();
		ActionSupport actionSupport = null;
		try {
			actionSupport = (ActionSupport) ClassUtil.getObjectByName(ac
					.getClassName());
		} catch (Exception e) {
			throw new ActionExecuteException("配置错误！" + name
					+ "对应的action的class属性配置的类为空或不存在！");
		}
		actionSupport.setAction(name);
		injectActionAttribute(actionSupport);
		// 注入收集表单的值
		injectForm(actionSupport, request, ac);
		return actionSupport;
	}

	@SuppressWarnings("unchecked")
	private void injectActionAttribute(ActionSupport actionSupport)
			throws ActionExecuteException {
		if (actionSupport instanceof HttpServletRequestAware) {
			HttpServletRequestAware hsra = (HttpServletRequestAware) actionSupport;
			hsra.setHttpServletRequest(request);
		}
		if (actionSupport instanceof HttpServletResponseAware) {
			HttpServletResponseAware hsra = (HttpServletResponseAware) actionSupport;
			hsra.setHttpServletResponse(response);
		}

		// 注入文件上传的值
		if (actionSupport instanceof FilePathAware) {
			FilePathAware fpa = (FilePathAware) actionSupport;
			fpa.setFilePath((Map<String, String>) request
					.getAttribute(ConfigLibrary.MULTIPART_FILEDATA));
			request.removeAttribute(ConfigLibrary.MULTIPART_FILEDATA);
		}
	}

	/**
	 * 注入表单值对象
	 * 
	 * @param actionSupport
	 * @param action
	 * @param request
	 * @param ac
	 * @param ac
	 * @throws ActionExecuteException
	 */
	private void injectForm(ActionSupport actionSupport,
			HttpServletRequest request, ActionConfig ac)
			throws ActionExecuteException {
		Field[] fields = actionSupport.getClass().getDeclaredFields();
		for (Field field : fields) {
			// 获取字段的类型
			Class<?> cls = field.getType();
			// 若是自动装入表单
			if (field.isAnnotationPresent(AutoWire.class)) {
				String viewName = cls.getSimpleName();
				// 若是该字段的类型是Object或者是List类型，则判断config对象是否为空
				// 若config为空则报异常，否则进行收集表单
				if (cls == Object.class || cls == List.class) {
					// 获取action的配置文件
					if (ac.getMatch() == null) {
						throw new ActionExecuteException(
								"指定收集的对象不明确，请在action.xml中配置match参数！");
					}
					ac.setSplitAction(ActionHelper.processAction(name));
					viewName = ac.getSplitAction()[ac.getMatch() - 1];
					// TODO
					if(ac.getSplitAction().length == ac.getMatch()) {
						request.setAttribute("executeOperateGrant",
								ac.getSplitAction()[ac.getMatch() - ac.getSplitAction().length]);
					}else {
						request.setAttribute("executeOperateGrant",
								ac.getSplitAction()[ac.getMatch()]);
					}
				}
				// 收集表单的服务类
				FetchFormValueAdvice advice = new DefaultFetchFormValueAdvice(
						request, viewName);
				Object value = null;
				if (cls != List.class) {
					value = advice.fetchFormObjectValue();
				} else {
					value = advice.fetchFormListValue();
				}
				// 为当前字段赋值
				try {
					field.setAccessible(true);
					field.set(actionSupport, value);
				} catch (Exception e) {
					throw new ActionExecuteException(e);
				}
			}
		}

	}
}
