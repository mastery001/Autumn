package org.web.service.support;

import java.util.List;

/**
 * 存放数据的备忘录
 * @author mastery
 * @Time 2015-4-19 下午5:21:42
 * 
 */
public class DataMemento {
	
	private List<Object> list ;

	public DataMemento(List<Object> list) {
		super();
		this.list = list;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}
	
	
}
