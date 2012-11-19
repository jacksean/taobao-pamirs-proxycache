package com.taobao.pamirs.cache.load.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.CacheModule;
import com.taobao.pamirs.cache.load.AbstractCacheConfigService;
import com.taobao.pamirs.cache.load.verify.CacheConfigVerify;
import com.taobao.pamirs.cache.util.ConfigUtil;

/**
 * 本地加载缓存配置服务
 * 
 * @author poxiao.gj
 * @date 2012-11-13
 * 
 */
public class LocalConfigServiceImpl extends AbstractCacheConfigService {

	private List<String> configFilePaths;
	private boolean isInit = false;
	private CacheConfig cacheConfig;

	/**
	 * 进行启动缓存初始化加载
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		// 1. 加载配置
		List<CacheModule> cacheModules = getCacheModules();
		if (cacheModules.size() <= 0) {
			throw new Exception("非法的缓存配置，CacheModule列表为空");
		}

		CacheConfig cacheConfig = new CacheConfig();
		cacheConfig.setStoreType(getStoreType());
		cacheConfig.setStoreMapCleanTime(getMapCleanTime());
		cacheConfig.setStoreRegion(getStoreRegion());
		cacheConfig.setStoreTairNameSpace(getTairNameSpace());
		for (CacheModule cacheModule : cacheModules) {
			cacheConfig.getCacheBeans().addAll(cacheModule.getCacheBeans());
			cacheConfig.getCacheCleanBeans().addAll(
					cacheModule.getCacheCleanBeans());
		}

		// 2. 自动填充默认的配置
		ConfigUtil.autoFillCacheConfig(cacheConfig, applicationContext);

		// 3. 缓存配置合法性校验
		CacheConfigVerify cacheConfigVerify = new CacheConfigVerify(
				applicationContext);
		if (!cacheConfigVerify.checkCacheConfig(cacheConfig)) {
			throw new Exception("缓存加载配置时，配置校验失败");
		}
		this.cacheConfig = cacheConfig;
		this.isInit = true;

		// 初始化cache
		super.initCache();
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

	/**
	 * 加载加载缓存配置
	 * 
	 * @return
	 */
	public CacheConfig loadConfig() {
		if (this.isInit) {
			return cacheConfig;
		}
		throw new RuntimeException("缓存初始化未完成，isInit=false");
	}

	public void setConfigFilePaths(List<String> configFilePaths) {
		this.configFilePaths = configFilePaths;
	}
}
