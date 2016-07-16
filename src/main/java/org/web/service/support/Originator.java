package org.web.service.support;

import java.util.List;

public class Originator {
	
	private List<Object> list;
	
	public DataMemento createMemento() {
		return new DataMemento( list);
	}
	
	public void setMemento(DataMemento dm) {
		this.list = dm.getList();
	}
 
	public void set(List<Object> list ) {
		this.setList(list);
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}
	
}
