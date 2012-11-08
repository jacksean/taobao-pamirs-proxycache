package com.taobao.pamirs.cache.store.tair;

import java.io.Serializable;

import com.taobao.pamirs.cache.framework.CacheException;
import com.taobao.pamirs.cache.framework.ICache;
import com.taobao.tair.DataEntry;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;

/**
 * TairStore �����Ա� Tair ��ͳһ����洢����.
 * <p>
 * 
 * <pre>
 * ͨ�� Key-Value ����ʽ�����л���Ķ������ Tair ��������.
 * ֻ�ܲ��� PUT , GET , REMOVE ������ Key ����. ����ʹ�� CLEAR , CLEAN ���ַ�Χ�������.
 * ʹ�ø� Store . ���������ԱȽϴ�. 5G ~ 10G ���Ǳ����ǹ̶�����. ���ݲ��ᷢ���仯.������ʷ��������.
 * ������������Ϣ. ���� ��Ʒ������Ϣ. ������ø� Store. ��Ҫ�����жϻ��������Ƿ�ʱ. �����ʱ. �򷵻� Null �൱�ڻ���û������.
 * Ȼ���ⲿ Cache ������ݿ��ȡ����. Ȼ������ Store . �������ϵĹ�������.
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
			DataEntry tairData = result.getValue();// ��һ��getValue����DataEntry����
			if (tairData == null) // �����ϲ������
				return null;

			return (V) tairData.getValue();// �ڶ���getValue����������value
		}

		// ʧ��
		throw new CacheException(result.getRc().getCode(), result.getRc()
				.getMessage());
	}

	@Override
	public void put(K key, V value) {
		// putǰ��Ҫremove
		remove(key);

		ResultCode rc = tairManager.put(namespace, key, value);

		// ʧ��
		if (!rc.isSuccess()) {
			throw new CacheException(rc.getCode(), rc.getMessage());
		}
	}

	@Override
	public void put(K key, V value, int expireTime) {
		// putǰ��Ҫremove
		remove(key);

		ResultCode rc = tairManager.put(namespace, key, value, 0, expireTime);

		// ʧ��
		if (!rc.isSuccess()) {
			throw new CacheException(rc.getCode(), rc.getMessage());
		}
	}

	@Override
	public void remove(K key) {
		// TODO xiaocheng
		// �������ʧЧ ����Ϊ �շ���ϵͳ�� �����ڲ�ͬ�ļ�Ⱥ�У����֣�

		ResultCode rc = tairManager.invalid(namespace, key);

		// ʧ��
		if (!rc.isSuccess()) {
			throw new CacheException(rc.getCode(), rc.getMessage());
		}
	}

	@Override
	public void clear() {
		throw new RuntimeException("Tair�洢 ��֧�ִ˷���");
	}

	@Override
	public int size() {
		throw new RuntimeException("Tair�洢 ��֧�ִ˷���");
	}

}
