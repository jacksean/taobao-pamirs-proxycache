package com.taobao.pamirs.cache.load;

import com.taobao.pamirs.cache.framework.config.CacheConfig;

/**
 * �������û�ȡ�ӿ�
 * 
 * @author xiaocheng 2012-11-12
 */
public interface ICacheConfigService {

	/**
	 * ����config
	 * 
	 * @return
	 */
	CacheConfig loadConfig();

}
