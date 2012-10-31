package com.taobao.pamirs.cache.store.map;

import java.io.Serializable;

/**
 * ��������װ��
 * 
 * @author xuanyu
 * @author xiaocheng 2012-10-30
 */
public class StoreObject<V> implements Serializable {
	//
	private static final long serialVersionUID = 2186360043715004471L;

	/**
	 * ʧЧʱ�䣨����ʱ�䣩����λ����<br>
	 * Null��ʾ����ʧЧ
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
		// �Ѿ�ʧЧ
		if (expireTime != null
				&& System.currentTimeMillis() > expireTime.longValue()) {
			return null;
		}

		return this.value;
	}
}
