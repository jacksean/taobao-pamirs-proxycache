package com.taobao.pamirs.cache.load;

import com.taobao.pamirs.cache.framework.config.CacheConfig;

/**
 * 缓存配置获取接口
 * 
 * @author xiaocheng 2012-11-12
 */
public interface ICacheConfigService {

	/**
	 * 加载config
	 * 
	 * @return
	 */
	CacheConfig loadConfig();

}
