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
	 * ����洢����
	 */
	private String storeType;
	
	/**
	 * tair����ռ䡾defaultΪ-1��
	 */
	private int tairNameSpace = -1;
	
	/**
	 * ���ػ�������ʱ��
	 */
	private String mapCleanTime;
	
	/**
	 * ���滷������
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
