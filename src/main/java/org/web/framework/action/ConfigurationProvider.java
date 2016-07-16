package org.web.framework.action;

import org.web.framework.action.config.Config;

public interface ConfigurationProvider {
	
	Config getConfig(String name);
}
