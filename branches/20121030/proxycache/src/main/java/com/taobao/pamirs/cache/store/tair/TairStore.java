package com.taobao.pamirs.cache.store.tair;

import java.io.Serializable;

import com.taobao.pamirs.cache.framework.CacheException;
import com.taobao.pamirs.cache.framework.ICache;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;

/**
 * TairStore 采用淘宝 Tair 的统一缓存存储方案.
 * <p>
 * 
 * <pre>
 * 通过 Key-Value 的形式将序列化后的对象存入 Tair 服务器中.
 * 只能采用 PUT , GET , REMOVE 这三种 Key 操作. 不能使用 CLEAR , CLEAN 这种范围清除操作.
 * 使用该 Store . 数据量可以比较大. 5G ~ 10G 但是必须是固定数据. 数据不会发生变化.例如历史订单数据.
 * 对于配置类信息. 例如 产品配置信息. 如果采用该 Store. 需要采用判断缓存数据是否超时. 如果超时. 则返回 Null 相当于缓存没有命中.
 * 然后外部 Cache 会从数据库获取数据. 然后存入该 Store . 覆盖以上的过期数据.
 * </pre>
 * 
 * @author xuanyu
 * @author xiaocheng 2012-11-2
 */
public class TairStore<K extends Serializable, V extends Serializable>
		implements ICache<K, V> {

	private TairManager tairManager;

	private int namespace;

	public TairStore(TairManager tairManager, int namespace) {
		this.tairManager = tairManager;
		this.namespace = namespace;
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(K key) {

		Result<DataEntry> result = tairManager.get(namespace, key);
		if (result.isSuccess()) {
			DataEntry tairData = result.getValue();// 第一个getValue返回DataEntry对象
			if (tairData == null) // 理论上不会出现
				return null;

			return (V) tairData.getValue();// 第二个getValue返回真正的value
		}

		// 失败
		throw new CacheException(result.getRc().getCode(), result.getRc()
				.getMessage());
	}

	@Override
	public void put(K key, V value) {
		// put前需要remove
		remove(key);

		ResultCode rc = tairManager.put(namespace, key, value);

		// 失败
		if (!rc.isSuccess()) {
			throw new CacheException(rc.getCode(), rc.getMessage());
		}
	}

	@Override
	public void put(K key, V value, int expireTime) {
		// put前需要remove
		remove(key);

		ResultCode rc = tairManager.put(namespace, key, value, 0, expireTime);

		// 失败
		if (!rc.isSuccess()) {
			throw new CacheException(rc.getCode(), rc.getMessage());
		}
	}

	@Override
	public void remove(K key) {
		// TODO xiaocheng
		// 这里采用失效 是因为 收费线系统将 会有在不同的集群中（容灾）

		ResultCode rc = tairManager.invalid(namespace, key);

		// 失败
		if (!rc.isSuccess()) {
			throw new CacheException(rc.getCode(), rc.getMessage());
		}
	}

	@Override
	public void clear() {
		throw new RuntimeException("Tair存储 不支持此方法");
	}

	@Override
	public int size() {
		throw new RuntimeException("Tair存储 不支持此方法");
	}

}
