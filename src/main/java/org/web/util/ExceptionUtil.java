package org.web.util;

public class ExceptionUtil {

	public static <T extends Throwable> T initNewCause(Throwable oldcause,
			T newcause) {
		newcause.initCause(oldcause);
		return newcause;
	}

	public static String formatStackTrace(Throwable e) {
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] trace = e.getStackTrace();
		for (int i = 0; i < trace.length; i++)
			sb.append("\n" + trace[i]);
		return sb.toString();
	}
}
