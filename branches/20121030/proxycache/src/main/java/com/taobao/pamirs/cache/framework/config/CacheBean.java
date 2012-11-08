package com.taobao.pamirs.cache.framework.config;

/**
 * 缓存bean配置
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheBean extends MethodConfig {

	//
	private static final long serialVersionUID = 4973185401294689002L;

	/**
	 * 失效时间，单位：秒。<br>
	 * 可以是相对时间，也可以是绝对时间(大于当前时间戳是绝对时间过期)。不传或0都是不过期
	 */
	private Integer expiredTime;

	public Integer getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Integer expiredTime) {
		this.expiredTime = expiredTime;
	}

}
