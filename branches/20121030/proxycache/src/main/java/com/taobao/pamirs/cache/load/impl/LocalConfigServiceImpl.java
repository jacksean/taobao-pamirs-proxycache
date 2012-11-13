package com.taobao.pamirs.cache.load.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.CacheModule;
import com.taobao.pamirs.cache.load.verify.CacheConfigVerify;
import com.taobao.pamirs.cache.util.ConfigUtil;

/**
 * 本地加载缓存配置服务
 * 
 * @author Administrator
 * 
 */
public class LocalConfigServiceImpl extends AbstractCacheConfigService {

	private List<String> configFilePaths;

	/**
	 * 加载加载缓存配置
	 * 
	 * @return
	 */
	public CacheConfig loadConfig() throws Exception {
		CacheConfig cacheConfig = new CacheConfig();
		cacheConfig.setStoreType(getStoreType());
		cacheConfig.setStoreMapCleanTime(getMapCleanTime());
		cacheConfig.setStoreRegion(getStoreRegion());
		cacheConfig.setStoreTairNameSpace(getTairNameSpace());
		List<CacheModule> cacheModules = getCacheModules();
		if (cacheModules.size() <= 0) {
			throw new IllegalArgumentException("非法的缓存配置");
		}
		for (CacheModule cacheModule : cacheModules) {
			cacheConfig.getCacheBeans().addAll(cacheModule.getCacheBeans());
			cacheConfig.getCacheCleanBeans().addAll(
					cacheModule.getCacheCleanBeans());
		}
		// 缓存配置合法性校验
		CacheConfigVerify cacheConfigVerify = new CacheConfigVerify(
				getApplicationContext());
		if (!cacheConfigVerify.checkCacheConfig(cacheConfig)) {
			return null;
		}
		return cacheConfig;
	}

	/**
	 * 从文件中获取配置文件信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CacheModule> getCacheModules() throws Exception {
		if (configFilePaths == null || configFilePaths.size() <= 0) {
			throw new IllegalArgumentException("非法配置文件路径的参数，配置文件列表不能为空");
		}
		InputStream input = null;
		try {
			ClassLoader classLoader = Thread.class.getClassLoader();
			if (classLoader == null) {
				classLoader = LocalConfigServiceImpl.class.getClassLoader();
			}
			List<CacheModule> cacheModuleList = new ArrayList<CacheModule>();
			for (String configFilePath : configFilePaths) {
				input = classLoader.getResourceAsStream(configFilePath);
				if (input != null) {
					CacheModule cacheModule = ConfigUtil
							.getCacheConfigModule(input);
					cacheModuleList.add(cacheModule);
				}
			}
			return cacheModuleList;
		} finally {
			if (input != null) {
				input.close();
			}
		}
	}

	public void setConfigFilePaths(List<String> configFilePaths) {
		this.configFilePaths = configFilePaths;
	}
}
