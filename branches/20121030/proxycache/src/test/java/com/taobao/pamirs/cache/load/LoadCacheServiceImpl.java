package com.taobao.pamirs.cache.load;

/**
 * 加载缓存服务
 * 
 * @author poxiao.gj
 * 
 */
public class LoadCacheServiceImpl implements LoadCacheSerivce {

	public String getCacheName(String name) {
		return "缓存名称：" + name;
	}

	public String getCacheCount(Long count) {
		return "缓存计数：" + count;
	}

	@Override
	public void cleanCacheByName(String name) {
		
	}

	@Override
	public void cleanCacheByCount(Long count) {
		
	}

}
