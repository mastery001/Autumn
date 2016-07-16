package org.web.framework.action.support;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
class TokenInterceptor {

	private static final TokenInterceptor INSTANCE = new TokenInterceptor();

	private TokenInterceptor() {
	}

	public static TokenInterceptor getInstance() {
		return INSTANCE;
	}

	public boolean validateToken(String action, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("token") == null) {
			return false;
		}
		boolean flag = isRepeatSubmit(request, session);
		if (flag) {
			CacheAction ca = map.get(action);
			if(ca == null) {
				return false;
			}
			request.getRequestDispatcher(ca.dispatcherPath).forward(
					ca.restoreRequestAttribute(request), response);
			return true;
		}
		request.getSession().removeAttribute("token");
		return flag;
	}

	/**
	 * 生成Token Token：Nv6RRuGEVvmGjB+jimI/gw==
	 * 
	 * @return
	 */
	public String makeToken(HttpServletRequest request, String action,
			String dispatcherPath) { // checkException
		// 7346734837483 834u938493493849384 43434384
		String token = (System.currentTimeMillis() + new Random()
				.nextInt(999999999)) + "";
		// 数据指纹 128位长 16个字节 md5
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte md5[] = md.digest(token.getBytes());
			// base64编码--任意二进制编码明文字符 adfsdfsdfsf
			BASE64Encoder encoder = new BASE64Encoder();
			token = encoder.encode(md5);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		map.put(action, new CacheAction(request, dispatcherPath));
		request.getSession().setAttribute("token", token);
		return token;
	}

	/*
	 * private void printAttribute(HttpServletRequest request) {
	 * System.out.println(request); Enumeration<String> enu =
	 * request.getAttributeNames(); while (enu.hasMoreElements()) {
	 * System.out.println("name is " + enu.nextElement()); } }
	 */

	/**
	 * 判断客户端提交上来的令牌和服务器端生成的令牌是否一致
	 * 
	 * @param request
	 * @return true 用户重复提交了表单 false 用户没有重复提交表单
	 */
	private boolean isRepeatSubmit(HttpServletRequest request,
			HttpSession session) {
		String client_token = request.getParameter("token");
		// 1、如果用户提交的表单数据中没有token，则用户是重复提交了表单
		if (client_token == null) {
			return true;
		}
		// 取出存储在Session中的token
		String server_token = (String) session.getAttribute("token");
		// 2、如果当前用户的Session中不存在Token(令牌)，则用户是重复提交了表单
		if (server_token == null) {
			return true;
		}
		// 3、存储在Session中的Token(令牌)与表单提交的Token(令牌)不同，则用户是重复提交了表单
		if (!client_token.equals(server_token)) {
			return true;
		}

		return false;
	}

	private class CacheAction {

		List<Object> requestAttribute;

		String dispatcherPath;

		CacheAction(HttpServletRequest request, String dispatcherPath) {
			this.dispatcherPath = dispatcherPath;
			requestAttribute = new ArrayList<Object>();
			fetchRequestAttribute(request);
		}

		private void fetchRequestAttribute(HttpServletRequest request) {
			Enumeration<String> enu = request.getAttributeNames();
			while (enu.hasMoreElements()) {
				String name = enu.nextElement();
				requestAttribute.add(name);
				requestAttribute.add(request.getAttribute(name));
			}
		}

		public HttpServletRequest restoreRequestAttribute(
				HttpServletRequest request) {
			for (int i = 0; i < requestAttribute.size(); i = i + 2) {
				request.setAttribute(requestAttribute.get(i).toString(),
						requestAttribute.get(i + 1));
			}
			return request;
		}

	}

	private static final Map<String, CacheAction> map = new HashMap<String, CacheAction>();
}
