package com.taobao.pamirs.cache2.framework.listener;

/**
 * �������������
 * 
 * @author xiaocheng 2012-10-31
 */
public interface CacheOprateListener {

	/**
	 * ����֪ͨ
	 * 
	 * @param oprator
	 * @param cacheInfo
	 */
	void oprate(CacheOprator oprator, CacheInfo cacheInfo);

}
