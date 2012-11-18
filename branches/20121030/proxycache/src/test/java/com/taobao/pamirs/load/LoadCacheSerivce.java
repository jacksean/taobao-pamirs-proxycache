package com.taobao.pamirs.load;
/**
 * 测试缓存
 * 
 * @author poxiao.gj
 *
 */
public interface LoadCacheSerivce {

	
	
	/**
	 * 获取缓存名称
	 * 
	 * @param name
	 * @return
	 */
	public String getCacheName(String name);
	
	/**
	 * 获取缓存计数
	 * 
	 * @param count
	 * @return
	 */
	public String getCacheCount(Long count);
	
	
	/**
	 * 清理缓存
	 * 
	 * @param name
	 */
	public void cleanCacheByName(String name);
	
	/**
	 * 清理缓存
	 * @param count
	 */
	public void cleanCacheByCount(Long count);
}
