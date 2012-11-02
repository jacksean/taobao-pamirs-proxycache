package com.taobao.pamirs.cache2.framework.config;

/**
 * ª∫¥Êbean≈‰÷√
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheBean extends MethodConfig {

	//
	private static final long serialVersionUID = 4973185401294689002L;

	private Integer expiredTime;

	public Integer getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Integer expiredTime) {
		this.expiredTime = expiredTime;
	}

}
