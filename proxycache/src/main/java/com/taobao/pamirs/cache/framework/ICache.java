/**
 * 
 */
package com.taobao.pamirs.cache.framework;

import java.io.Serializable;

/**
 * 缓存支持接口
 * 
 * @author xuanyu
 * @author xiaocheng 2012-10-30
 */
public interface ICache<K extends Serializable, V extends Serializable> {

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
	 * @param key
	 * @param value
	 * @param useVersion 是否使用数据版本控制(tair支持， 会多访问一次get)
	 */
	public void put(K key, V value,boolean useVersion);

	/**
	 * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
	 * 
	 * @param key
	 * @param value
	 * @param expireTime 数据的有效时间（绝对时间），单位毫秒
	 * @param useVersion 是否使用数据版本控制(tair支持， 会多访问一次get)
	 *            
	 */
	public void put(K key, V value, int expireTime,boolean useVersion);

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
	
	
	/**
	 * 隐藏数据
	 * @param key
	 */
	public void hidden(K key);

}
