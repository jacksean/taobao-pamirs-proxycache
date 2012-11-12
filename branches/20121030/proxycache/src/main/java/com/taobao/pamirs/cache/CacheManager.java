package com.taobao.pamirs.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.taobao.pamirs.cache.framework.CacheProxy;
import com.taobao.pamirs.cache.framework.ICache;
import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.framework.timer.CleanCacheTimerManager;
import com.taobao.pamirs.cache.jmx.CacheMbean;
import com.taobao.pamirs.cache.jmx.CacheMbeanListener;
import com.taobao.pamirs.cache.jmx.mbean.MBeanManagerFactory;
import com.taobao.pamirs.cache.load.ILoadConfig;
import com.taobao.pamirs.cache.store.StoreType;
import com.taobao.pamirs.cache.store.map.MapStore;
import com.taobao.pamirs.cache.store.tair.TairStore;
import com.taobao.pamirs.cache.util.CacheCodeUtil;
import com.taobao.tair.TairManager;

/**
 * 缓存框架入口类
 * 
 * @author xuanyu
 * @author xiaocheng 2012-11-2
 */
public abstract class CacheManager implements ApplicationContextAware ,ILoadConfig {

	private static final Log log = LogFactory.getLog(CacheManager.class);

	private CacheConfig cacheConfig;

	/**
	 * 每一个method对应一个adapter实例
	 */
	private final Map<String, CacheProxy<Serializable, Serializable>> cacheProxys = new ConcurrentHashMap<String, CacheProxy<Serializable, Serializable>>();

	private TairManager tairManager;

	private ApplicationContext applicationContext;

	private CleanCacheTimerManager timeTask;

	private boolean useCache = true;

	/**
	 * 缓存分区（可选）
	 */
	private String storeRegion;

	/**
	 * Tair命名空间（just for tair）
	 * 
	 * @see StoreType.TAIR
	 */
	private int storeTairNameSpace;

	/**
	 * spring定义时的初始化方法
	 */
	public void init() {
		// 1. 加载/校验config
		cacheConfig = loadConfig();
		
		// 2. 初始化缓存
		List<CacheBean> cacheBeans = cacheConfig.getCacheBeans();
		if (cacheBeans != null) {
			// 只需注册cacheBean,目前cacheCleanBeans必须是它的子集
			for (CacheBean bean : cacheBeans) {

				List<MethodConfig> cacheMethods = bean.getCacheMethods();
				for (MethodConfig method : cacheMethods) {
					initCacheAdapters(storeRegion, bean.getBeanName(), method,
							cacheConfig.getStoreMapCleanTime());
				}

			}
		}

		timeTask = new CleanCacheTimerManager();
	}

	/**
	 * 初始化Bean/Method对应的缓存，包括： <br>
	 * 1. CacheProxy <br>
	 * 2. 定时清理任务：storeMapCleanTime <br>
	 * 3. 注册JMX <br>
	 * 
	 * @param region
	 * @param cacheBean
	 * @param storeMapCleanTime
	 */
	private void initCacheAdapters(String region, String beanName,
			MethodConfig cacheMethod, String storeMapCleanTime) {
		String key = CacheCodeUtil.getCacheAdapterKey(region, beanName,
				cacheMethod);
		StoreType storeType = StoreType.toEnum(cacheConfig.getStoreType());
		ICache<Serializable, Serializable> cache = null;

		if (StoreType.TAIR == storeType) {
			cache = new TairStore<Serializable, Serializable>(tairManager,
					storeTairNameSpace);
		} else if (StoreType.MAP == storeType) {
			cache = new MapStore<Serializable, Serializable>();
		}

		if (cache != null) {
			CacheProxy<Serializable, Serializable> cacheProxy = new CacheProxy<Serializable, Serializable>(
					storeType, key, cache, beanName, cacheMethod);

			cacheProxys.put(key, cacheProxy);

			// 定时清理任务：storeMapCleanTime
			if (StoreType.MAP == storeType
					&& StringUtils.isNotBlank(storeMapCleanTime)) {
				try {
					timeTask.createCleanCacheTask(cacheProxy, storeMapCleanTime);
				} catch (Exception e) {
					log.error("[严重]设置Map定时清理任务失败!", e);
				}
			}

			// 注册JMX
			registerCacheMbean(key, cacheProxy, storeMapCleanTime,
					cacheMethod.getExpiredTime());
		}
	}

	/**
	 * 注册JMX
	 * 
	 * @param key
	 * @param cacheProxy
	 */
	private void registerCacheMbean(String key,
			CacheProxy<Serializable, Serializable> cacheProxy,
			String storeMapCleanTime, Integer expiredTime) {
		try {
			String mbeanName = CacheMbean.MBEAN_NAME + ":name=" + key;
			CacheMbeanListener listener = new CacheMbeanListener();
			cacheProxy.addListener(listener);
			CacheMbean<Serializable, Serializable> cacheMbean = new CacheMbean<Serializable, Serializable>(
					cacheProxy, listener, applicationContext,
					storeMapCleanTime, expiredTime);
			MBeanManagerFactory.registerMBean(mbeanName, cacheMbean);
		} catch (Exception e) {
			log.error("注册JMX失败", e);
		}
	}
	
	@Override
	public abstract CacheConfig loadConfig();

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
