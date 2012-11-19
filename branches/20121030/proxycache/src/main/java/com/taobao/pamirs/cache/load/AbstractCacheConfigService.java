package com.taobao.pamirs.cache.load;

import com.taobao.pamirs.cache.CacheManager;

/**
 * 缓存抽象公共
 * 
 * @author poxiao.gj
 * @date 2012-11-13
 */
public abstract class AbstractCacheConfigService extends CacheManager {

	/**
	 * 缓存存储类型
	 */
	private String storeType;

	/**
	 * tair储存空间【default为-1】
	 */
	private int tairNameSpace = -1;

	/**
	 * 本地缓存清理时间
	 */
	private String mapCleanTime;

	/**
	 * 缓存环境隔离
	 */
	private String storeRegion;

	public String getStoreType() {
		return storeType;
	}

	public int getTairNameSpace() {
		return tairNameSpace;
	}

	public String getMapCleanTime() {
		return mapCleanTime;
	}

	public String getStoreRegion() {
		return storeRegion;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public void setTairNameSpace(int tairNameSpace) {
		this.tairNameSpace = tairNameSpace;
	}

	public void setMapCleanTime(String mapCleanTime) {
		this.mapCleanTime = mapCleanTime;
	}

	public void setStoreRegion(String storeRegion) {
		this.storeRegion = storeRegion;
	}

}
