package com.taobao.pamirs.cache.framework.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.taobao.pamirs.cache.store.StoreType;

/**
 * 缓存总配置
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheConfig implements Serializable {

	//
	private static final long serialVersionUID = 8164876688008497503L;

	/**
	 * 缓存类型
	 * 
	 * @see StoreType
	 */
	private String storeType;

	/**
	 * Map自动清理表达式（just for map）
	 * 
	 * @see StoreType.MAP
	 */
	private String storeMapCleanTime;

	/**
	 * 缓存分区（可选）
	 */
	private String storeRegion;

	/**
	 * Tair命名空间（just for tair）
	 * 
	 * @see StoreType.TAIR
	 */
	private int storeTairNameSpace;

	/**
	 * 缓存bean配置
	 */
	private List<CacheBean> cacheBeans = new ArrayList<CacheBean>();

	/**
	 * 清理缓存bean配置
	 */
	private List<CacheCleanBean> cacheCleanBeans = new ArrayList<CacheCleanBean>();

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreMapCleanTime() {
		return storeMapCleanTime;
	}

	public void setStoreMapCleanTime(String storeMapCleanTime) {
		this.storeMapCleanTime = storeMapCleanTime;
	}

	public String getStoreRegion() {
		return storeRegion;
	}

	public void setStoreRegion(String storeRegion) {
		this.storeRegion = storeRegion;
	}

	public int getStoreTairNameSpace() {
		return storeTairNameSpace;
	}

	public void setStoreTairNameSpace(int storeTairNameSpace) {
		this.storeTairNameSpace = storeTairNameSpace;
	}

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
