package com.taobao.pamirs.cache.framework.config;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author Administrator
 *
 */
public class CacheModule implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1194734355755304229L;

	/**
	 * ª∫¥Êbean≈‰÷√
	 */
	private List<CacheBean> cacheBeans;

	/**
	 * «Â¿Ìª∫¥Êbean≈‰÷√
	 */
	private List<CacheCleanBean> cacheCleanBeans;

	public List<CacheBean> getCacheBeans() {
		return cacheBeans;
	}

	public void setCacheBeans(List<CacheBean> cacheBeans) {
		this.cacheBeans = cacheBeans;
	}

	public List<CacheCleanBean> getCacheCleanBeans() {
		return cacheCleanBeans;
	}

	public void setCacheCleanBeans(List<CacheCleanBean> cacheCleanBeans) {
		this.cacheCleanBeans = cacheCleanBeans;
	}

}
