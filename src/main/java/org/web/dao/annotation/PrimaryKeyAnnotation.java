package org.web.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 对po类主键的注解
 * @author mastery
 * @Time 2015-3-8 下午11:44:57
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PrimaryKeyAnnotation {
	
	String id();
}
