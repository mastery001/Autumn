package org.web.framework.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.action.support.DefaultActionInvocation;
import org.web.framework.action.support.DefaultActionMapper;
import org.web.servlet.support.UploadFileAtttibute;

public class ActionConfiguration {

	private ConfigurationProvider configurationProvider;

	public ActionInvocation createActionInvocation(String name,
			HttpServletRequest request, HttpServletResponse response) {
		return new DefaultActionInvocation(this, name, request, response);
	}

	public ActionMapper getActionMapper() {
		return new DefaultActionMapper(this);
	}

	public ConfigurationProvider getConfigurationProvider() {
		return configurationProvider;
	}

	public void setConfigurationProvider(
			ConfigurationProvider configurationProvider) {
		this.configurationProvider = configurationProvider;
	}

	public UploadFileAtttibute getUploadFileAtttibute() {
		return UploadFileAtttibute.updateUploadFileAtttibute();
	}
}
