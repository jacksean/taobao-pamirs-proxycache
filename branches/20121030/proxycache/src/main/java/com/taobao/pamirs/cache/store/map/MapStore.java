/**
 * 
 */
package com.taobao.pamirs.cache.store.map;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.pamirs.cache.framework.ICache;

/**
 * MapStore 使用本地 Map 作为 CacheManage 的缓存存储方案.
 * <p>
 * 
 * <pre>
 * 通过 Key-Value 的形式将对象存入 本地内存中.
 * 可以采用 PUT , GET , REMOVE 这三种 Key 操作. 可以采用 CLEAR , CLEAN 这种范围清除操作.
 * 使用该 Store . 数据量较小. 0 ~ 1G 访问耗时极低. 适用于数据量小但变化较多的场合.例如基础型数据.
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
