package com.taobao.pamirs.cache.framework.config;

import java.io.Serializable;
import java.util.List;

/**
 * ����bean����
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheBean implements Serializable {

	//
	private static final long serialVersionUID = 4973185401294689002L;

	private String beanName;

	/**
	 * ����ķ����б�
	 */
	private List<MethodConfig> cacheMethods;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public List<MethodConfig> getCacheMethods() {
		return cacheMethods;
	}

	public void setCacheMethods(List<MethodConfig> cacheMethods) {
		this.cacheMethods = cacheMethods;
	}

}
