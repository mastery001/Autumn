package org.web.servlet.adapter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.web.framework.ConfigLibrary;
import org.web.servlet.FileUploadAdvice;
import org.web.servlet.support.UploadFileAtttibute;

class FileUploadProxyRequestAdvice extends AbstractProxyRequestAdvice {

	private FileUploadAdvice fileUploadAdvice;

	public FileUploadProxyRequestAdvice(HttpServletRequest request,
			UploadFileAtttibute ufa) throws IOException {
		super(request);
		this.fileUploadAdvice = new DefaultFileUploadAdvice(request,
				ufa.getMaxSize(), ufa.getTempDir());
		// 执行上传操作
		request.setAttribute(ConfigLibrary.MULTIPART_FILEDATA,fileUploadAdvice.upload(ufa.getSavePath(),
				ufa.isModifyName()));
	}

	@Override
	public String getParameter(String name) {
		String returnVal = null; // 保存返回内容
		List<String> temp = this.fileUploadAdvice.getReqeustParams().get(name);
		if (temp != null) {
			returnVal = temp.get(0);
		} else {
			returnVal = this.request.getParameter(name);
		}
		return returnVal;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		Iterator<String> it = this.fileUploadAdvice.getReqeustParams().keySet()
				.iterator();
		Vector<String> keys = new Vector<String>();
		while (it.hasNext()) {
			keys.add(it.next());
		}
		if (keys.size() == 0) {
			return this.request.getParameterNames();
		}
		return keys.elements();
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] returnVal = null;
		List<String> temp = this.fileUploadAdvice.getReqeustParams().get(name);
		if (temp != null) {
			returnVal = temp.toArray(new String[] {}); // 将内容变成字符串数组
		} else {
			returnVal = this.request.getParameterValues(name);
		}
		return returnVal;
	}

}
