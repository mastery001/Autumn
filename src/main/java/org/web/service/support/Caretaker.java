package org.web.service.support;

import java.util.HashMap;
import java.util.Map;

public class Caretaker {
	
	private Map<String , DataMemento> map;

	public Caretaker() {
		map = new HashMap<String , DataMemento>();
	}
	
	public DataMemento getDm(String name) {
		return map.get(name);
	}

	public void setDm(String name , DataMemento dm) {
		map.put(name, dm);
	}
	
	
}
