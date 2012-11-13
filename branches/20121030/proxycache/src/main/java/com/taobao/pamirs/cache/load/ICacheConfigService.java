package com.taobao.pamirs.cache.load;

import java.util.List;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.store.StoreType;

/**
 * �������û�ȡ�ӿ�
 * 
 * @author xiaocheng 2012-11-12
 */
public interface ICacheConfigService {

	/**
	 * ����config
	 * 
	 * @return
	 */
	List<CacheConfig> loadConfig();

	/**
	 * �����������ѡ��
	 */
	String getStoreRegion();

	/**
	 * Tair�����ռ䣨just for tair��
	 * 
	 * @see StoreType.TAIR
	 */
	int getStoreTairNameSpace();

}
