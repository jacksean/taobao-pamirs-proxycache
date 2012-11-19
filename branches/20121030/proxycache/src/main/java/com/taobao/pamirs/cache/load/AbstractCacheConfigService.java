package com.taobao.pamirs.cache.load;

import com.taobao.pamirs.cache.CacheManager;

/**
 * ������󹫹�
 * 
 * @author poxiao.gj
 * @date 2012-11-13
 */
public abstract class AbstractCacheConfigService extends CacheManager {

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

}
