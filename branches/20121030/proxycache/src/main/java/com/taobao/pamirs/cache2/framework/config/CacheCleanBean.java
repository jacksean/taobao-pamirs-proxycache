package com.taobao.pamirs.cache2.framework.config;

import java.util.List;

/**
 * ��������bean����
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheCleanBean extends MethodConfig {

	//
	private static final long serialVersionUID = -4582877908557906265L;

	/**
	 * ��Ҫremove����ķ����б�
	 */
	private List<MethodConfig> cleanCodes;

	public List<MethodConfig> getCleanCodes() {
		return cleanCodes;
	}

	public void setCleanCodes(List<MethodConfig> cleanCodes) {
		this.cleanCodes = cleanCodes;
	}

}
