package com.taobao.pamirs.cache2.framework.config;

import java.util.List;

/**
 * 缓存清理bean配置
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheCleanBean extends MethodConfig {

	//
	private static final long serialVersionUID = -4582877908557906265L;

	/**
	 * 需要remove缓存的方法列表
	 */
	private List<MethodConfig> cleanCodes;

	public List<MethodConfig> getCleanCodes() {
		return cleanCodes;
	}

	public void setCleanCodes(List<MethodConfig> cleanCodes) {
		this.cleanCodes = cleanCodes;
	}

}
