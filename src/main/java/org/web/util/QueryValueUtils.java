package org.web.util;

import java.util.ArrayList;
import java.util.List;

import org.web.dao.core.support.Page;

/**
 * 查询值的工具类
 * @author mastery
 * @Time 2015-4-14 下午7:12:29
 * 
 */
public class QueryValueUtils {
	
	public static List<Object> getListByPage(List<Object> list , Page page) {
		List<Object> retList = new ArrayList<Object>();
		// 得到需要的几条数据
		int firstIndex = page.getFirstIndex();
		int maxSize = page.getMaxSize();
		if (maxSize != 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i >= firstIndex && i < firstIndex + maxSize) {
					retList.add(list.get(i));
				}
			}
		} else {
			return list;
		}
		return retList;
	}
}
