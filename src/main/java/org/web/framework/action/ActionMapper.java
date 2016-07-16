package org.web.framework.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.web.framework.action.support.ActionMapping;

public interface ActionMapper {
	
	ActionMapping getMapping(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException ;
}
