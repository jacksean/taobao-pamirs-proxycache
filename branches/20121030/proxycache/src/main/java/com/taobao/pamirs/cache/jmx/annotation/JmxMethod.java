package com.taobao.pamirs.cache.jmx.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * �Ƿ���Ҫ��¶��JmxMethod
 * </p>
 * 
 * @author:shixian
 * 
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface JmxMethod {

}
