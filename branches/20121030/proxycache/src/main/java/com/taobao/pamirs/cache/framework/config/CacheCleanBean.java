package com.taobao.pamirs.cache.framework.config;

import java.io.Serializable;
import java.util.List;

/**
 * 缓存清理bean配置
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheCleanBean implements Serializable {

	//
	private static final long serialVersionUID = -4582877908557906265L;

	private String beanName;

	/**
	 * 需要清理的原生方法
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
