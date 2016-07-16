package org.web.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对po类表名的注解
 * @author mastery
 * @Time 2015-3-8 下午11:45:11
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableAnnotation {
	
	String value();
}
