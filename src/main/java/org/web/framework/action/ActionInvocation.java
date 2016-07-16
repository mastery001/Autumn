package org.web.framework.action;

import org.web.exception.ActionExecuteException;

public interface ActionInvocation {
	
	Object getAction() throws ActionExecuteException;
}
