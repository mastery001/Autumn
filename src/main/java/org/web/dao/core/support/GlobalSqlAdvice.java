package org.web.dao.core.support;

import org.web.dao.core.SqlAdvice;

public class GlobalSqlAdvice {
	
	public static final SqlAdvice INSTANCE = new DefaultSqlAdvice();
	
	public static final SqlAdvice getInstance() {
		return INSTANCE;
	}
}
