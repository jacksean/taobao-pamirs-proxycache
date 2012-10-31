/**
 * 
 */
package com.taobao.pamirs.cache.framework;

/**
 * 
 * �洢�ӿ�
 * 
 * @author xuanyu
 * @author xiaocheng 2012-10-30
 */
public interface Store<K, V> {

	public static String STORE_TYPE_MAP = "map";
	public static String STORE_TYPE_TAIR = "tair";

	/**
	 * ��ȡ����
	 * 
	 * @param key
	 * @return
	 */
	public V get(K key);

	/**
	 * �������ݣ���������Ѿ����ڣ��򸲸ǣ���������ڣ�������
	 * 
	 * @param key
	 * @param value
	 */
	public void put(K key, V value);

	/**
	 * �������ݣ���������Ѿ����ڣ��򸲸ǣ���������ڣ�������
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            ���ݵ���Чʱ�䣨����ʱ�䣩����λ����
	 */
	public void put(K key, V value, long expireTime);

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

}
