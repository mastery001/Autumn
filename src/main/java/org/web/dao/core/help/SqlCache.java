package org.web.dao.core.help;

import java.util.HashMap;
import java.util.Map;

import org.web.dao.core.HelpAdvice;
import org.web.dao.core.support.VoResolve;
import org.web.exception.VoProcessorException;


public class SqlCache {
	public static final Map<Class<?>, String> VOSQL = new HashMap<Class<?>, String>();

	public static String getVoSql(HelpAdvice advice , VoResolve voResolve)
			throws VoProcessorException {
		Class<?> voClass = voResolve.getVoClass();
		if (voClass == null) {
			throw new VoProcessorException("voClass不能为空");
		}
		if (!VOSQL.containsKey(voClass)) {
			VOSQL.put(voClass, advice.getVoSql(voResolve));
		}
		return VOSQL.get(voClass);
	}

}
