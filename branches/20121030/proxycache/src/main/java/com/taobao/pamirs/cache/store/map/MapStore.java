/**
 * 
 */
package com.taobao.pamirs.cache.store.map;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.pamirs.cache.framework.ICache;

/**
 * MapStore ʹ�ñ��� Map ��Ϊ CacheManage �Ļ���洢����.
 * <p>
 * 
 * <pre>
 * ͨ�� Key-Value ����ʽ��������� �����ڴ���.
 * ���Բ��� PUT , GET , REMOVE ������ Key ����. ���Բ��� CLEAR , CLEAN ���ַ�Χ�������.
 * ʹ�ø� Store . ��������С. 0 ~ 1G ���ʺ�ʱ����. ������������С���仯�϶�ĳ���.�������������.
 * </pre>
 * 
 * @author xuanyu
 * @author xiaocheng 2012-11-2
 */
public class MapStore<K extends Serializable, V extends Serializable>
		implements ICache<K, V> {

	private final ConcurrentHashMap<K, ObjectBoxing<V>> datas = new ConcurrentHashMap<K, ObjectBoxing<V>>();

	@Override
	public V get(K key) {
		ObjectBoxing<V> storeObject = datas.get(key);
		if (storeObject == null)
			datas.remove(key, storeObject);

		return storeObject.getObject();
	}

	@Override
	public void put(K key, V value) {
		ObjectBoxing<V> storeObject = new ObjectBoxing<V>(value);
		datas.putIfAbsent(key, storeObject);
	}

	@Override
	public void put(K key, V value, int expireTime) {
		ObjectBoxing<V> storeObject = new ObjectBoxing<V>(value, expireTime);
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
