package org.web.framework.action.config;

import java.util.List;

public class Config {
	
	private ActionConfig actionConfig;
	
	private List<ResultConfig> resultConfig;

	public ActionConfig getActionConfig() {
		return actionConfig;
	}

	public void setActionConfig(ActionConfig actionConfig) {
		this.actionConfig = actionConfig;
	}

	public List<ResultConfig> getResultConfig() {
		return resultConfig;
	}

	public void setResultConfig(List<ResultConfig> resultConfig) {
		this.resultConfig = resultConfig;
	}

	@Override
	public String toString() {
		return "Config [actionConfig=" + actionConfig + ", resultConfig="
				+ resultConfig + "]";
	}
	
}
