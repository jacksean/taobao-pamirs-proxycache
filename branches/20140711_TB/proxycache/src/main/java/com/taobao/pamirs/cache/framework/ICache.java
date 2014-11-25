/**
 * 
 */
package com.taobao.pamirs.cache.framework;

import java.io.Serializable;

/**
 * ����֧�ֽӿ�
 * 
 * @author xuanyu
 * @author xiaocheng 2012-10-30
 */
public interface ICache<K extends Serializable, V extends Serializable> {

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * �������ݣ���������Ѿ����ڣ��򸲸ǣ���������ڣ�������
	 * @param key
	 * @param value
	 * @param useVersion �Ƿ�ʹ�����ݰ汾����(tair֧�֣� ������һ��get)
	 */
	public void put(K key, V value,boolean useVersion);

	/**
	 * �������ݣ���������Ѿ����ڣ��򸲸ǣ���������ڣ�������
	 * 
	 * @param key
	 * @param value
	 * @param expireTime ���ݵ���Чʱ�䣨����ʱ�䣩����λ����
	 * @param useVersion �Ƿ�ʹ�����ݰ汾����(tair֧�֣� ������һ��get)
	 *            
	 */
	public void put(K key, V value, int expireTime,boolean useVersion);

	/**
	 * ɾ��key��Ӧ������
	 * 
	 * @param key
	 */
	public void remove(K key);

	/**
	 * ������е�����
	 */
	public void clear();

	/**
	 * ��ȡ����������
	 * 
	 * @return
	 */
	public int size();
	
	
	/**
	 * ��������
	 * @param key
	 */
	public void hidden(K key);

}
