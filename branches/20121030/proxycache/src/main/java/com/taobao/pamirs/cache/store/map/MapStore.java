/**
 * 
 */
package com.taobao.pamirs.cache.store.map;

import java.util.concurrent.ConcurrentHashMap;

import com.taobao.pamirs.cache.framework.Store;

/**
 * MapStore ʹ�ñ��� Map ��Ϊ CacheManage �Ļ���洢����. ͨ�� Key-Value ����ʽ��������� �����ڴ���.
 * 
 * ���Բ��� PUT , GET , REMOVE ������ Key ����. ���Բ��� CLEAR , CLEAN ���ַ�Χ�������.
 * 
 * ͨ�� MQ TOPIC ���л���������ͨ��.������Ϣ.�Ӷ����»���
 * 
 * ʹ�ø� Store . ��������С. 0 ~ 1G ���ʺ�ʱ����. ������������С���仯�϶�ĳ���.
 * 
 * �������������.
 * 
 * **/
public class MapStore<K, V> implements Store<K, V> {

	private final ConcurrentHashMap<K, StoreObject<V>> datas = new ConcurrentHashMap<K, StoreObject<V>>();

	@Override
	public V get(K key) {
		StoreObject<V> storeObject = datas.get(key);
		if (storeObject == null)
			datas.remove(key, storeObject);

		return storeObject.getObject();
	}

	@Override
	public void put(K key, V value) {
		StoreObject<V> storeObject = new StoreObject<V>(value);
		datas.putIfAbsent(key, storeObject);
	}

	@Override
	public void put(K key, V value, long expireTime) {
		StoreObject<V> storeObject = new StoreObject<V>(value, expireTime);
		datas.putIfAbsent(key, storeObject);
	}

	@Override
	public void remove(K key) {
		datas.remove(key);
	}

	@Override
	public void clear() {
		datas.clear();
	}

	@Override
	public int size() {
		return datas.size();
	}

}
