package org.web.servlet.adapter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.web.servlet.support.UploadFileAtttibute;

class DefaultRequestAdapter implements RequestAdapter {

	@Override
	public boolean isFileForm(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}

	@Override
	public HttpServletRequest parseRequest(HttpServletRequest request,
			UploadFileAtttibute ufa) throws IOException {
		if (isFileForm(request)) {
			return new FileUploadProxyRequestAdvice(request, ufa);
		} else {
			return new DefaultProxyRequestAdvice(request);
		}
	}

}
