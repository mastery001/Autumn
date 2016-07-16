package org.web.servlet.adapter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.web.servlet.support.UploadFileAtttibute;

public interface RequestAdapter {
	
	boolean isFileForm(HttpServletRequest request);
	
	HttpServletRequest parseRequest(HttpServletRequest request , UploadFileAtttibute ufa) throws IOException;
}
