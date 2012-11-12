package com.taobao.pamirs.cache.framework.config;

import java.util.List;

/**
 * 缓存清理的方法，内部包含关联的clean methods
 * 
 * @author xiaocheng 2012-11-12
 */
public class CacheCleanMethod extends MethodConfig {

	//
	private static final long serialVersionUID = 2983763433924222529L;

	/**
	 * 需要关联remove缓存的方法列表
	 */
	private List<MethodConfig> cleanMethods;

	public List<MethodConfig> getCleanMethods() {
		return cleanMethods;
	}

	public void setCleanMethods(List<MethodConfig> cleanMethods) {
		this.cleanMethods = cleanMethods;
	}

}
