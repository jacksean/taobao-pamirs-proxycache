package com.taobao.pamirs.cache.load.impl;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.load.AbstractCacheConfigService;

public class ZKConfigCacheManager extends AbstractCacheConfigService {

	public void init() {
		// ....

		// ��ʼ��cache
		super.initCache();
	}

	@Override
	public CacheConfig loadConfig() {
		// TODO Auto-generated method stub
		return null;
	}

}
