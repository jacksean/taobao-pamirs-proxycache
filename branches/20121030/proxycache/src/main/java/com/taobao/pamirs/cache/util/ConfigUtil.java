package com.taobao.pamirs.cache.util;

import java.util.List;

import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;

/**
 * 配置辅助类
 * 
 * @author xiaocheng 2012-11-2
 */
public class ConfigUtil {

	/**
	 * 是否bean在cache中配置
	 * 
	 * @param cacheConfig
	 * @param beanName
	 * @return
	 */
	public static boolean isBeanHaveCache(CacheConfig cacheConfig,
			String beanName) {
		if (cacheConfig == null || beanName == null)
			return false;
		
		List<CacheBean> cacheBeans = cacheConfig.getCacheBeans();
		List<CacheCleanBean> cacheCleanBeans = cacheConfig.getCacheCleanBeans();

		if ((cacheBeans == null || cacheBeans.size() == 0)
				&& (cacheCleanBeans == null || cacheCleanBeans.size() == 0))
			return false;

		for (CacheBean bean : cacheBeans) {
			if (beanName.equals(bean.getBeanName()))
				return true;
		}

		for (CacheCleanBean bean : cacheCleanBeans) {
			if (beanName.equals(bean.getBeanName()))
				return true;
		}

		return false;
	}

	/**
	 * 获取method对应的CacheBean配置
	 * 
	 * @param cacheConfig
	 * @param beanName
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static CacheBean getCacheBean(CacheConfig cacheConfig,
			String beanName, String methodName, List<Class<?>> parameterTypes) {

		List<CacheBean> cacheBeans = cacheConfig.getCacheBeans();

		return (CacheBean) getMethodConfig(cacheBeans, cacheConfig, beanName,
				methodName, parameterTypes);
	}

	/**
	 * 获取method对应的CacheCleanBean配置
	 * 
	 * @param cacheConfig
	 * @param beanName
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static CacheCleanBean getCacheClean(CacheConfig cacheConfig,
			String beanName, String methodName, List<Class<?>> parameterTypes) {

		List<CacheCleanBean> cacheCleanBeans = cacheConfig.getCacheCleanBeans();

		return (CacheCleanBean) getMethodConfig(cacheCleanBeans, cacheConfig,
				beanName, methodName, parameterTypes);
	}

	private static MethodConfig getMethodConfig(
			List<? extends MethodConfig> list, CacheConfig cacheConfig,
			String beanName, String methodName, List<Class<?>> parameterTypes) {

		if (cacheConfig == null || beanName == null || methodName == null)
			return null;

		if (list == null || list.isEmpty())
			return null;

		for (MethodConfig bean : list) {
			if (bean.isMe(beanName, methodName, parameterTypes))
				return bean;
		}

		return null;
	}

}
