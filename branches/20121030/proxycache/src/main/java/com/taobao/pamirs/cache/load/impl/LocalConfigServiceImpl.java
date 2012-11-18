package com.taobao.pamirs.cache.load.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.CacheModule;
import com.taobao.pamirs.cache.load.verify.CacheConfigVerify;
import com.taobao.pamirs.cache.util.ConfigUtil;

/**
 * ���ؼ��ػ������÷���
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
	 * ���ؼ��ػ�������
	 * 
	 * @return
	 */
	public CacheConfig loadConfig() {
		if (this.isInit) {
			return cacheConfig;
		}
		throw new RuntimeException("�����ʼ��δ��ɣ�isInit=false");
	}

	/**
	 * �������������ʼ������
	 * 
	 * @throws Exception
	 */
	public void init() throws Exception {
		CacheConfig cacheConfig = new CacheConfig();
		cacheConfig.setStoreType(getStoreType());
		cacheConfig.setStoreMapCleanTime(getMapCleanTime());
		cacheConfig.setStoreRegion(getStoreRegion());
		cacheConfig.setStoreTairNameSpace(getTairNameSpace());
		List<CacheModule> cacheModules = getCacheModules();
		if (cacheModules.size() <= 0) {
			throw new Exception("�Ƿ��Ļ������ã�CacheModule�б�Ϊ��");
		}
		for (CacheModule cacheModule : cacheModules) {
			cacheConfig.getCacheBeans().addAll(cacheModule.getCacheBeans());
			cacheConfig.getCacheCleanBeans().addAll(
					cacheModule.getCacheCleanBeans());
		}
		// �Զ����Ĭ�ϵ�����
		autoFillConfig(cacheConfig);
		// �������úϷ���У��
		CacheConfigVerify cacheConfigVerify = new CacheConfigVerify(
				getApplicationContext());
		if (!cacheConfigVerify.checkCacheConfig(cacheConfig)) {
			throw new Exception("�����������ʱ������У��ʧ��");
		}
		this.cacheConfig = cacheConfig;
		this.isInit = true;
	}

	/**
	 * ���ļ��л�ȡ�����ļ���Ϣ
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CacheModule> getCacheModules() throws Exception {
		if (configFilePaths == null || configFilePaths.size() <= 0) {
			throw new IllegalArgumentException("�Ƿ������ļ�·���Ĳ����������ļ��б���Ϊ��");
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
