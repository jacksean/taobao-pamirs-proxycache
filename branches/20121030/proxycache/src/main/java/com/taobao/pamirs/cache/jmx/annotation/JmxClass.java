package com.taobao.pamirs.cache.jmx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * �Ժ�jmx��ע�ᣬ��ͨ��ע���Զ�ע�룬����ҪӲ����
 * 
 * @author wuxiang
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JmxClass {

}
