package com.taobao.pamirs.cache.load;

import java.util.List;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.store.StoreType;

/**
 * 缓存配置获取接口
 * 
 * @author xiaocheng 2012-11-12
 */
public interface ICacheConfigService {

	/**
	 * 加载config
	 * 
	 * @return
	 */
	List<CacheConfig> loadConfig();

	/**
	 * 缓存分区（可选）
	 */
	String getStoreRegion();

	/**
	 * Tair命名空间（just for tair）
	 * 
	 * @see StoreType.TAIR
	 */
	int getStoreTairNameSpace();

}
