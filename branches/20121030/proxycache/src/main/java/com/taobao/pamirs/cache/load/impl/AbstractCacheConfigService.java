package com.taobao.pamirs.cache.load.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.taobao.pamirs.cache.load.ICacheConfigService;
/**
 * 
 * @author Administrator
 *
 */
public abstract class AbstractCacheConfigService implements ICacheConfigService, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
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
	
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
