/**
 * 
 */
package com.taobao.pamirs.cache.framework;

/**
 * 
 * 存储接口
 * 
 * @author xuanyu
 * @author xiaocheng 2012-10-30
 */
public interface Store<K, V> {

	public static String STORE_TYPE_MAP = "map";
	public static String STORE_TYPE_TAIR = "tair";

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value);

	/**
	 * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            数据的有效时间（绝对时间），单位毫秒
	 */
	public void put(K key, V value, long expireTime);

	/**
	 * 删除key对应的数据
	 * 
	 * @param key
	 */
	public void remove(K key);

	/**
	 * 清除所有的数据
	 */
	public void clear();

	/**
	 * 获取缓存数据量
	 * 
	 * @return
	 */
	public int size();

}
