package org.web.dao.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对po类外键的注解
 * @author mastery
 * @Time 2015-3-22 下午8:16:27
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ForeignKeyAnnotation {
	
	/**
	 * 为是外键的属性进行注解，注解内容应是其表名
	 * @return
	 */
	String value();
}
