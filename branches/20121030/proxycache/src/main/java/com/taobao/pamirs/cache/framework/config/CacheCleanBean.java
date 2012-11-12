package com.taobao.pamirs.cache.framework.config;

import java.io.Serializable;
import java.util.List;

/**
 * ��������bean����
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheCleanBean implements Serializable {

	//
	private static final long serialVersionUID = -4582877908557906265L;

	private String beanName;

	/**
	 * ��Ҫ�����ԭ������
	 */
	private List<CacheCleanMethod> methods;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public List<CacheCleanMethod> getMethods() {
		return methods;
	}

	public void setMethods(List<CacheCleanMethod> methods) {
		this.methods = methods;
	}

}
