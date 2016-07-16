package org.web.servlet.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * 代理request对象的这三个方法服务,其他方法按原来方式执行
 * @author mastery
 * @Time 2015-3-22 下午1:59:49
 * 
 */
abstract class AbstractProxyRequestAdvice implements HttpServletRequest {

	protected HttpServletRequest request;

	public AbstractProxyRequestAdvice(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public AsyncContext getAsyncContext() {
		return request.getAsyncContext();
	}

	@Override
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return request.getAttributeNames();
	}

	@Override
	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	@Override
	public int getContentLength() {
		return request.getContentLength();
	}

	@Override
	public String getContentType() {
		return request.getContentType();
	}

	@Override
	public DispatcherType getDispatcherType() {
		return request.getDispatcherType();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	@Override
	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	@Override
	public String getLocalName() {
		return request.getLocalName();
	}

	@Override
	public int getLocalPort() {
		return request.getLocalPort();
	}

	@Override
	public Locale getLocale() {
		return request.getLocale();
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return request.getLocales();
	}
	
	// TODO Auto-generated method stub
	public abstract String getParameter(String name);
	

	@Override
	public Map<String, String[]> getParameterMap() {
		return request.getParameterMap();
	}

	// TODO Auto-generated method stub
	@Override
	public abstract Enumeration<String> getParameterNames();

	// TODO Auto-generated method stub
	@Override
	public abstract String[] getParameterValues(String name);

	@Override
	public String getProtocol() {
		return request.getProtocol();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getRealPath(String name) {
		return request.getRealPath(name);
	}

	@Override
	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	@Override
	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	@Override
	public int getRemotePort() {
		return request.getRemotePort();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String path) {
		return request.getRequestDispatcher(path);
	}

	@Override
	public String getScheme() {
		return request.getScheme();
	}

	@Override
	public String getServerName() {
		return request.getServerName();
	}

	@Override
	public int getServerPort() {
		return request.getServerPort();
	}

	@Override
	public ServletContext getServletContext() {
		return request.getServletContext();
	}

	@Override
	public boolean isAsyncStarted() {
		return request.isAsyncStarted();
	}

	@Override
	public boolean isAsyncSupported() {
		return false;
	}

	@Override
	public boolean isSecure() {
		return false;
	}

	@Override
	public void removeAttribute(String name) {
		request.removeAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object o) {
		request.setAttribute(name, o);
	}

	@Override
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException {
		request.setCharacterEncoding(env);
	}

	@Override
	public AsyncContext startAsync() throws IllegalStateException {
		return request.startAsync();
	}

	@Override
	public AsyncContext startAsync(ServletRequest servletRequest,
			ServletResponse servletResponse) throws IllegalStateException {
		return request.startAsync(servletRequest, servletResponse);
	}

	@Override
	public boolean authenticate(HttpServletResponse response) throws IOException,
			ServletException {
		return request.authenticate(response);
	}

	@Override
	public String getAuthType() {
		return request.getAuthType();
	}

	@Override
	public String getContextPath() {
		return request.getContextPath();
	}

	@Override
	public Cookie[] getCookies() {
		return request.getCookies();
	}

	@Override
	public long getDateHeader(String name) {
		return request.getDateHeader(name);
	}

	@Override
	public String getHeader(String name) {
		return request.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return request.getHeaderNames();
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		return request.getHeaders(name);
	}

	@Override
	public int getIntHeader(String name) {
		return request.getIntHeader(name);
	}

	@Override
	public String getMethod() {
		return request.getMethod();
	}

	@Override
	public Part getPart(String name) throws IOException, ServletException {
		return request.getPart(name);
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return request.getParts();
	}

	@Override
	public String getPathInfo() {
		return request.getPathInfo();
	}

	@Override
	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	@Override
	public String getQueryString() {
		return request.getQueryString();
	}

	@Override
	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	@Override
	public String getRequestURI() {
		return request.getRequestURI();
	}

	@Override
	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	@Override
	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	@Override
	public String getServletPath() {
		return request.getServletPath();
	}

	@Override
	public HttpSession getSession() {
		return request.getSession();
	}

	@Override
	public HttpSession getSession(boolean name) {
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return request.isRequestedSessionIdFromUrl();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	@Override
	public boolean isUserInRole(String role) {
		return request.isUserInRole(role);
	}

	@Override
	public void login(String username, String password) throws ServletException {
		request.login(username, password);
	}

	@Override
	public void logout() throws ServletException {
		request.logout();
	}

}
