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
 * �����������
 * 
 * @author xuanyu
 * @author xiaocheng 2012-11-2
 */
public abstract class CacheManager implements ApplicationContextAware ,ILoadConfig {

	private static final Log log = LogFactory.getLog(CacheManager.class);

	private CacheConfig cacheConfig;

	/**
	 * ÿһ��method��Ӧһ��adapterʵ��
	 */
	private final Map<String, CacheProxy<Serializable, Serializable>> cacheProxys = new ConcurrentHashMap<String, CacheProxy<Serializable, Serializable>>();

	private TairManager tairManager;

	private ApplicationContext applicationContext;

	private CleanCacheTimerManager timeTask;

	private boolean useCache = true;

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

	/**
	 * spring����ʱ�ĳ�ʼ������
	 */
	public void init() {
		// 1. ����/У��config
		cacheConfig = loadConfig();
		
		// 2. ��ʼ������
		List<CacheBean> cacheBeans = cacheConfig.getCacheBeans();
		if (cacheBeans != null) {
			// ֻ��ע��cacheBean,ĿǰcacheCleanBeans�����������Ӽ�
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
	 * ��ʼ��Bean/Method��Ӧ�Ļ��棬������ <br>
	 * 1. CacheProxy <br>
	 * 2. ��ʱ��������storeMapCleanTime <br>
	 * 3. ע��JMX <br>
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

			// ��ʱ��������storeMapCleanTime
			if (StoreType.MAP == storeType
					&& StringUtils.isNotBlank(storeMapCleanTime)) {
				try {
					timeTask.createCleanCacheTask(cacheProxy, storeMapCleanTime);
				} catch (Exception e) {
					log.error("[����]����Map��ʱ��������ʧ��!", e);
				}
			}

			// ע��JMX
			registerCacheMbean(key, cacheProxy, storeMapCleanTime,
					cacheMethod.getExpiredTime());
		}
	}

	/**
	 * ע��JMX
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
			log.error("ע��JMXʧ��", e);
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
