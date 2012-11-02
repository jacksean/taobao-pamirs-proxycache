package com.taobao.pamirs.cache2.framework.aop.advice;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.pamirs.cache2.CacheManager;
import com.taobao.pamirs.cache2.framework.CacheAdapter;
import com.taobao.pamirs.cache2.framework.config.CacheBean;
import com.taobao.pamirs.cache2.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache2.framework.config.CacheConfig;
import com.taobao.pamirs.cache2.framework.config.MethodConfig;
import com.taobao.pamirs.cache2.util.CacheCodeUtil;
import com.taobao.pamirs.cache2.util.ConfigUtil;

/**
 * 通知处理类
 * 
 * @author xuannan
 * @author xiaocheng 2012-10-30
 */
public class CacheManagerRoundAdvice implements MethodInterceptor, Advice {

	private static final Log log = LogFactory
			.getLog(CacheManagerRoundAdvice.class);

	private CacheManager cacheManager;
	private String beanName;

	public CacheManagerRoundAdvice(CacheManager cacheManager, String beanName) {
		this.cacheManager = cacheManager;
		this.beanName = beanName;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		CacheBean cacheBean = null;
		CacheCleanBean cacheCleanBean = null;
		String storeRegion;

		try {
			CacheConfig cacheConfig = cacheManager.getCacheConfig();

			Method method = invocation.getMethod();
			String methodName = method.getName();
			Class<?>[] parameterTypes = method.getParameterTypes();

			cacheBean = ConfigUtil.getCacheBean(cacheConfig, methodName,
					methodName, Arrays.asList(parameterTypes));
			cacheCleanBean = ConfigUtil.getCacheClean(cacheConfig, methodName,
					methodName, Arrays.asList(parameterTypes));

			storeRegion = cacheConfig.getStoreRegion();

		} catch (Exception e) {
			log.error("CacheManager:切面解析配置出错:" + beanName + "#"
					+ invocation.getMethod().getName(), e);
			return invocation.proceed();
		}

		try {
			// 1. cache
			if (cacheManager.isUseCache() && cacheBean != null) {
				String adapterKey = CacheCodeUtil.getCacheAdapterKey(
						storeRegion, cacheBean);
				CacheAdapter<String, Serializable> cacheAdapter = cacheManager
						.getCacheAdapter(adapterKey);

				String cacheCode = CacheCodeUtil.getCacheCode(storeRegion,
						cacheBean, invocation);

				return useCache(cacheAdapter, cacheCode,
						cacheBean.getExpiredTime(), invocation);
			}

			// 2. cache clean
			if (cacheCleanBean != null) {
				try {
					return invocation.proceed();
				} finally {
					cleanCache(cacheCleanBean, invocation, storeRegion);
				}
			}

			// 3. do nothing
			return invocation.proceed();
		} catch (Exception e) {
			log.error("CacheManager:出错:" + beanName + "#"
					+ invocation.getMethod().getName() + e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 缓存处理
	 * 
	 * @param cacheAdapter
	 * @param cacheCode
	 * @param expireTime
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	private Object useCache(CacheAdapter<String, Serializable> cacheAdapter,
			String cacheCode, Integer expireTime, MethodInvocation invocation)
			throws Throwable {
		if (cacheAdapter == null)
			return invocation.proceed();

		Object response = cacheAdapter.get(cacheCode);

		if (response == null) {
			response = invocation.proceed();

			if (response == null)// 如果原生方法结果为null，不put到缓存了
				return response;

			if (expireTime == null) {
				cacheAdapter.put(cacheCode, (Serializable) response);
			} else {
				cacheAdapter
						.put(cacheCode, (Serializable) response, expireTime);
			}
		}

		return response;
	}

	/**
	 * 清除缓存处理
	 * 
	 * @param cacheCleanBean
	 * @param invocation
	 * @param storeRegion
	 * @return
	 * @throws Throwable
	 */
	private void cleanCache(CacheCleanBean cacheCleanBean,
			MethodInvocation invocation, String storeRegion) throws Throwable {
		List<MethodConfig> cleanCodes = cacheCleanBean.getCleanCodes();
		if (cleanCodes != null && !cleanCodes.isEmpty()) {
			for (MethodConfig methodConfig : cleanCodes) {

				String adapterKey = CacheCodeUtil.getCacheAdapterKey(
						storeRegion, methodConfig);
				CacheAdapter<String, Serializable> cacheAdapter = cacheManager
						.getCacheAdapter(adapterKey);

				if (cacheAdapter != null) {
					String cacheCode = CacheCodeUtil.getCacheCode(storeRegion,
							methodConfig, invocation);// 这里的invocation直接用主bean的，因为清理的bean的参数必须和主bean保持一致
					cacheAdapter.remove(cacheCode);
				}
			}
		}
	}

}
