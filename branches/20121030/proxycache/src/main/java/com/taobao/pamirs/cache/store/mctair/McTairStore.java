package com.taobao.pamirs.cache.store.mctair;

import java.io.Serializable;

import com.taobao.pamirs.cache.framework.ICache;

public class McTairStore<K extends Serializable, V extends Serializable>
		implements ICache<K, V> {

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(K key, V value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void put(K key, V value, int expireTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(K key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		throw new RuntimeException("McTair存储 不支持此方法");
	}

	@Override
	public int size() {
		throw new RuntimeException("McTair存储 不支持此方法");
	}

}
