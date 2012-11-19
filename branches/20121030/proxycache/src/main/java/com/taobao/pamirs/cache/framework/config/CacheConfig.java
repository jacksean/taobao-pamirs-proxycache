package com.taobao.pamirs.cache.framework.config;

import com.taobao.pamirs.cache.store.StoreType;

/**
 * ����������
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheConfig extends CacheModule {

	//
	private static final long serialVersionUID = 8164876688008497503L;

	/**
	 * ��������
	 * 
	 * @see StoreType
	 */
	private String storeType;

	/**
	 * Map�Զ�������ʽ��just for map��
	 * 
	 * @see StoreType.MAP
	 */
	private String storeMapCleanTime;

	/**
	 * �����������ѡ��
	 */
	private String storeRegion;

	/**
	 * Tair�����ռ䣨just for tair��
	 * 
	 * @see StoreType.TAIR
	 */
	private int storeTairNameSpace;

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

}
