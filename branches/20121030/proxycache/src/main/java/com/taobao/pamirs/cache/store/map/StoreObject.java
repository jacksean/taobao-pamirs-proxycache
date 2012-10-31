package com.taobao.pamirs.cache.store.map;

import java.io.Serializable;

/**
 * 缓存对象包装类
 * 
 * @author xuanyu
 * @author xiaocheng 2012-10-30
 */
public class StoreObject<V> implements Serializable {
	//
	private static final long serialVersionUID = 2186360043715004471L;

	/**
	 * 失效时间（绝对时间），单位毫秒<br>
	 * Null表示永不失效
	 */
	private Long expireTime;

	private V value;

	public StoreObject(V value) {
		this(value, null);
	}

	public StoreObject(V value, Long expireTime) {
		this.value = value;
		this.expireTime = expireTime;
	}

	public V getObject() {
		// 已经失效
		if (expireTime != null
				&& System.currentTimeMillis() > expireTime.longValue()) {
			return null;
		}

		return this.value;
	}
}
