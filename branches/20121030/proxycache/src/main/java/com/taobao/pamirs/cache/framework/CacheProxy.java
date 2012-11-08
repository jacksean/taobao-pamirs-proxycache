package com.taobao.pamirs.cache.framework;

import static com.taobao.pamirs.cache.framework.listener.CacheOprator.GET;
import static com.taobao.pamirs.cache.framework.listener.CacheOprator.PUT;
import static com.taobao.pamirs.cache.framework.listener.CacheOprator.PUT_EXPIRE;
import static com.taobao.pamirs.cache.framework.listener.CacheOprator.REMOVE;

import java.io.Serializable;

import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.framework.listener.CacheInfo;
import com.taobao.pamirs.cache.framework.listener.CacheObservable;
import com.taobao.pamirs.cache.store.StoreType;

/**
 * 缓存处理适配器
 * 
 * @author xiaocheng 2012-10-31
 */
public class CacheProxy<K extends Serializable, V extends Serializable> extends
		CacheObservable implements ICache<K, V> {

	private StoreType storeType;
	private String key;

	/** 注入真正的cache实现 */
	private ICache<K, V> cache;
	private MethodConfig methodConfig;

	public CacheProxy(StoreType storeType, String key, ICache<K, V> cache,
			MethodConfig methodConfig) {
		this.storeType = storeType;
		this.key = key;
		this.cache = cache;
		this.methodConfig = methodConfig;
	}

	@Override
	public V get(K key) {
		if (!isUseCache)
			return null;

		CacheException cacheException = null;
		V v = null;

		long start = System.currentTimeMillis();
		try {
			v = cache.get(key);
		} catch (CacheException e) {
			cacheException = e;
		}

		long end = System.currentTimeMillis();

		boolean isHitting = v != null;// 是否命中，null即未命中

		// listener
		notifyListeners(GET, new CacheInfo(key, start - end, isHitting,
				methodConfig, cacheException));

		return v;
	}

	@Override
	public void put(K key, V value) {

		CacheException cacheException = null;

		long start = System.currentTimeMillis();
		try {
			cache.put(key, value);
		} catch (CacheException e) {
			cacheException = e;
		}

		long end = System.currentTimeMillis();

		// listener
		notifyListeners(PUT, new CacheInfo(key, start - end, true,
				methodConfig, cacheException));
	}

	@Override
	public void put(K key, V value, int expireTime) {
		CacheException cacheException = null;

		long start = System.currentTimeMillis();
		try {
			cache.put(key, value, expireTime);
		} catch (CacheException e) {
			cacheException = e;
		}
		long end = System.currentTimeMillis();

		// listener
		notifyListeners(PUT_EXPIRE, new CacheInfo(key, start - end, true,
				methodConfig, cacheException));
	}

	@Override
	public void remove(K key) {
		CacheException cacheException = null;

		long start = System.currentTimeMillis();
		try {
			cache.remove(key);
		} catch (CacheException e) {
			cacheException = e;
		}
		long end = System.currentTimeMillis();

		// listener
		notifyListeners(REMOVE, new CacheInfo(key, start - end, true,
				methodConfig, cacheException));
	}

	@Override
	public void clear() {
		cache.clear();
	}

	@Override
	public int size() {
		return cache.size();
	}

	/** 单个方法的缓存开关 */
	private boolean isUseCache = true;

	public void setIsUseCache(boolean isUseCache) {
		this.isUseCache = isUseCache;
	}
	
	public boolean isUseCache() {
		return isUseCache;
	}

	public void setUseCache(boolean isUseCache) {
		this.isUseCache = isUseCache;
	}

	public StoreType getStoreType() {
		return storeType;
	}

	public void setStoreType(StoreType storeType) {
		this.storeType = storeType;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
