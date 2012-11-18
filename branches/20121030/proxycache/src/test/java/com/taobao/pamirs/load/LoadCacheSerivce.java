package com.taobao.pamirs.load;
/**
 * ���Ի���
 * 
 * @author poxiao.gj
 *
 */
public interface LoadCacheSerivce {

	
	
	/**
	 * ��ȡ��������
	 * 
	 * @param name
	 * @return
	 */
	public String getCacheName(String name);
	
	/**
	 * ��ȡ�������
	 * 
	 * @param count
	 * @return
	 */
	public String getCacheCount(Long count);
	
	
	/**
	 * ������
	 * 
	 * @param name
	 */
	public void cleanCacheByName(String name);
	
	/**
	 * ������
	 * @param count
	 */
	public void cleanCacheByCount(Long count);
}
