package com.taobao.pamirs.cache2;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.taobao.pamirs.cache2.framework.CacheProxy;
import com.taobao.pamirs.cache2.framework.ICache;
import com.taobao.pamirs.cache2.framework.config.CacheBean;
import com.taobao.pamirs.cache2.framework.config.CacheConfig;
import com.taobao.pamirs.cache2.framework.config.MethodConfig;
import com.taobao.pamirs.cache2.store.StoreType;
import com.taobao.pamirs.cache2.store.map.MapStore;
import com.taobao.pamirs.cache2.store.tair.TairStore;
import com.taobao.pamirs.cache2.util.CacheCodeUtil;
import com.taobao.tair.TairManager;

/**
 * 缓存框架入口类
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheManager implements ApplicationContextAware {

	private CacheConfig cacheConfig;

	private boolean useCache = true;

	/**
	 * 每一个method对应一个adapter实例
	 */
	private final Map<String, CacheProxy<String, Serializable>> cacheAdapters = new ConcurrentHashMap<String, CacheProxy<String, Serializable>>();

	private TairManager tairManager;

	public void init() {
		String storeRegion = cacheConfig.getStoreRegion();
		List<CacheBean> cacheBeans = cacheConfig.getCacheBeans();
		// List<CacheCleanBean> cacheCleanBeans =
		// cacheConfig.getCacheCleanBeans();

		if (cacheBeans != null) {
			for (CacheBean bean : cacheBeans) {
				initCacheAdapters(storeRegion, bean);
			}
		}

		// if (cacheCleanBeans != null) {
		// for (CacheCleanBean bean : cacheCleanBeans) {
		// initCacheAdapters(storeRegion, bean);
		// }
		// }

	}

	private void initCacheAdapters(String region, MethodConfig methodConfig) {
		String key = CacheCodeUtil.getCacheAdapterKey(region, methodConfig);
		ICache<String, Serializable> cache = null;

		if (StoreType.TAIR == StoreType.toEnum(cacheConfig.getStoreType())) {
			cache = new TairStore<String, Serializable>(tairManager,
					cacheConfig.getStoreTairNameSpace());
		} else if (StoreType.MAP == StoreType
				.toEnum(cacheConfig.getStoreType())) {
			cache = new MapStore<String, Serializable>();
		}

		if (cache != null) {
			cacheAdapters.put(key, new CacheProxy<String, Serializable>(
					cache, methodConfig));
		}
	}

	public CacheProxy<String, Serializable> getCacheAdapter(String key) {
		if (key == null || cacheAdapters == null)
			return null;

		return cacheAdapters.get(key);
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setCacheConfig(CacheConfig cacheConfig) {
		this.cacheConfig = cacheConfig;
	}

	public CacheConfig getCacheConfig() {
		return cacheConfig;
	}

	public void setTairManager(TairManager tairManager) {
		this.tairManager = tairManager;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

	}

}
