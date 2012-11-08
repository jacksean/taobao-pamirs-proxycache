package com.taobao.pamirs.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.taobao.pamirs.cache.framework.CacheProxy;
import com.taobao.pamirs.cache.framework.ICache;
import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.jmx.CacheMbean;
import com.taobao.pamirs.cache.jmx.CacheMbeanListener;
import com.taobao.pamirs.cache.jmx.mbean.MBeanManagerFactory;
import com.taobao.pamirs.cache.store.StoreType;
import com.taobao.pamirs.cache.store.map.MapStore;
import com.taobao.pamirs.cache.store.tair.TairStore;
import com.taobao.pamirs.cache.util.CacheCodeUtil;
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
	private final Map<String, CacheProxy<Serializable, Serializable>> cacheProxys = new ConcurrentHashMap<String, CacheProxy<Serializable, Serializable>>();

	private TairManager tairManager;

	private ApplicationContext applicationContext;

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
		StoreType storeType = StoreType.toEnum(cacheConfig.getStoreType());
		ICache<Serializable, Serializable> cache = null;

		if (StoreType.TAIR == storeType) {
			cache = new TairStore<Serializable, Serializable>(tairManager,
					cacheConfig.getStoreTairNameSpace());
		} else if (StoreType.MAP == storeType) {
			cache = new MapStore<Serializable, Serializable>();
		}

		if (cache != null) {
			CacheProxy<Serializable, Serializable> cacheProxy = new CacheProxy<Serializable, Serializable>(
					storeType, key, cache, methodConfig);

			cacheProxys.put(key, cacheProxy);

			// 注册JMX
			registerCacheMbean(key, cacheProxy);
		}
	}

	/**
	 * 注册JMX
	 * 
	 * @param key
	 * @param cacheProxy
	 */
	private void registerCacheMbean(String key,
			CacheProxy<Serializable, Serializable> cacheProxy) {
		try {
			String mbeanName = "Pamirs-Cache:name=" + key;
			CacheMbeanListener listener = new CacheMbeanListener();
			cacheProxy.addListener(listener);
			CacheMbean<Serializable, Serializable> cacheMbean = new CacheMbean<Serializable, Serializable>(
					cacheProxy, listener, applicationContext);
			MBeanManagerFactory.registerMBean(mbeanName, cacheMbean);
		} catch (Exception e) {
			// TODO
			e.printStackTrace();
		}
	}

	public CacheProxy<Serializable, Serializable> getCacheProxys(String key) {
		if (key == null || cacheProxys == null)
			return null;

		return cacheProxys.get(key);
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
		this.applicationContext = applicationContext;
	}

}
