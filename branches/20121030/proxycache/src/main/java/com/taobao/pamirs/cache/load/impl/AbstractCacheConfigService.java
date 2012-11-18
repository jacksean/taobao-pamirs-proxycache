package com.taobao.pamirs.cache.load.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanMethod;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.load.ICacheConfigService;

/**
 * ������󹫹�
 * 
 * @author poxiao.gj
 * @date 2012-11-13
 */
public abstract class AbstractCacheConfigService implements
		ICacheConfigService, ApplicationContextAware {

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

	/**
	 * �Զ����Ĭ������
	 * 
	 * @param cacheConfig
	 */
	protected void autoFillConfig(CacheConfig cacheConfig) {

		// ���������Ĳ�������
		for (CacheCleanBean cleanBean : cacheConfig.getCacheCleanBeans()) {
			for (CacheCleanMethod cacheCleanMethod : cleanBean.getMethods()) {
				for (MethodConfig methodConfig : cacheCleanMethod
						.getCleanMethods()) {
					methodConfig.setParameterTypes(cacheCleanMethod
							.getParameterTypes());
				}
			}
		}
	}

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
