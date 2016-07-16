package org.web.framework.action.support;

import java.util.HashMap;
import java.util.Map;

/**
 * action的帮助类
 * @author mastery
 * @Time 2015-4-11 下午8:57:51
 * 
 */
public class ActionHelper {

	public static final String SPLIT_CHAR = ",";
	/**
	 * 存储对应action的分割符
	 */
	private static final Map<String, String[]> SPLIT_MAP = new HashMap<String, String[]>();

	private static String initAction(String action) {
		char[] chs = action.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			if (!String.valueOf(chs[i]).matches("[a-zA-Z]")) {
				if (action.contains(SPLIT_CHAR)) {
					action = action.replace(String.valueOf(chs[i]), " ");
					continue;
				}
				action = action.replace(String.valueOf(chs[i]), SPLIT_CHAR);
			}
		}
		return action;
	}

	/**
	 * 对FetchConfig对象进行初始化，若该对象为空则不需要初始化，判定其未配置match参数
	 * 否则需要设置viewName，来进行收集对应的表单对象
	 * 
	 * @param actionSupport
	 * @param action
	 * @param config
	 */
	public static String[] processAction(String action) {
		if (SPLIT_MAP.get(action) == null) {
			// 对action进行拆分保存
			String newAction = initAction(action);
			String[] splitAction = newAction.split(SPLIT_CHAR);
			SPLIT_MAP.put(action, splitAction);
		}
		return SPLIT_MAP.get(action);
	}
	
	public static String processRequestPath(String requestUri) {
		String key = requestUri.substring(requestUri.lastIndexOf("/") + 1);
		key = key.substring(0, key.lastIndexOf("."));
		return key;
	}
}
