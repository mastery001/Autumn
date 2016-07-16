package org.web.servlet.adapter;

public class GlobalRequestAdapter {
	
	public static final RequestAdapter REQUEST_ADAPTER = new DefaultRequestAdapter();
	
	public static RequestAdapter getInstance() {
		return REQUEST_ADAPTER;
	}
}
