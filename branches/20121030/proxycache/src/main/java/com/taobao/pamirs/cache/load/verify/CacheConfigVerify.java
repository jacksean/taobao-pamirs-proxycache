package com.taobao.pamirs.cache.load.verify;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanMethod;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;

/**
 * 缓存配置合法性校验
 * 
 * @author Administrator
 * 
 */
public class CacheConfigVerify {

	public static final Logger logger = Logger
			.getLogger(CacheConfigVerify.class);

	private ApplicationContext applicationContext;

	private Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public CacheConfigVerify(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * 对于缓存配置进行强校验
	 * 
	 * @param cacheConfig
	 * @return
	 */
	public boolean checkCacheConfig(CacheConfig cacheConfig) {
		if (!checkConfigNotEmpty(cacheConfig)) {
			return false;
		}
		if (!checkCacheBeanDuplicate(cacheConfig.getCacheBeans())
				|| !checkCacheCleanBeanDuplicate(cacheConfig
						.getCacheCleanBeans())) {
			return false;
		}
		if (!checkCacheBeanMethodInvalid(cacheConfig.getCacheBeans())
				|| !checkCacheCleanBeanMethodInvalid(cacheConfig
						.getCacheCleanBeans())) {
			return false;
		}
		return true;
	}

	/**
	 * 校验必要配置数据不能为空
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkConfigNotEmpty(CacheConfig cacheConfig) {
		if (cacheConfig == null) {
			logCheckInvalid("缓存配置数据CacheConfig为空");
			return false;
		}
		if (StringUtils.isBlank(cacheConfig.getStoreRegion())
				|| StringUtils.isBlank(cacheConfig.getStoreType())) {
			logCheckInvalid("缓存配置数据StoreRegion或者StoreType为空");
			return false;
		}
		if (cacheConfig.getCacheBeans() == null
				|| cacheConfig.getCacheBeans().isEmpty()) {
			logCheckInvalid("缓存配置数据CacheBeans为空");
			return false;
		}
		if (cacheConfig.getCacheCleanBeans() == null
				|| cacheConfig.getCacheCleanBeans().isEmpty()) {
			logCheckInvalid("缓存配置数据CacheCleanBeans为空");
			return false;
		}
		return true;
	}

	/**
	 * 判断缓存配置是否有重复配置
	 * 
	 * @param cacheBeans
	 * @return
	 */
	private boolean checkCacheBeanDuplicate(List<CacheBean> cacheBeans) {
		Map<String, Boolean> dup = new HashMap<String, Boolean>();
		for (CacheBean cacheBean : cacheBeans) {
			for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
				String key = cacheBean.getBeanName() + "."
						+ methodConfig.getMethodName();
				if (dup.containsKey(key)) {
					logCheckInvalid("缓存对象" + key + "重复定义");
					return false;
				}
				dup.put(key, Boolean.TRUE);
			}
		}
		return true;
	}

	/**
	 * 判断清理缓存是否有重复配置
	 * 
	 * @param cacheCleanBeans
	 * @return
	 */
	private boolean checkCacheCleanBeanDuplicate(
			List<CacheCleanBean> cacheCleanBeans) {
		Map<String, Boolean> dup = new HashMap<String, Boolean>();
		for (CacheCleanBean cacheCleanBean : cacheCleanBeans) {
			for (CacheCleanMethod cacheCleanMethod : cacheCleanBean
					.getMethods()) {
				String key = cacheCleanBean.getBeanName() + "."
						+ cacheCleanMethod.getMethodName();
				if (dup.containsKey(key)) {
					logCheckInvalid("清理缓存对象" + key + "重复定义");
					return false;
				}
				dup.put(key, Boolean.TRUE);
			}

		}
		return true;
	}

	/**
	 * 判断缓存的bean在spring中是否存在
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkCacheBeanMethodInvalid(List<CacheBean> cacheBeans) {
		if (false) {
			for (CacheBean cacheBean : cacheBeans) {
				Object bean = getBean(cacheBean.getBeanName());
				if (bean == null) {
					//
					return false;
				}
				for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
					if (methodConfig.getParameterTypes() == null
							|| methodConfig.getParameterTypes().size() <= 0) {
						// 缓存方法的参数不能为空
						return false;
					}
					
				}
			}
		}
		return true;
	}

	/**
	 * 判断
	 * 
	 * @param bean
	 * @param methodConfig
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isBeanMethodValid(Object bean, MethodConfig methodConfig) {
		Class<?>[] interfaces = bean.getClass().getInterfaces();
		for (Class<?> interface_ : interfaces) {
			Method[] methods = interface_.getClass().getMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodConfig.getMethodName())) {
					Class<?>[] paramTypes = method.getParameterTypes();
					for (int i = 0; i < methodConfig.getParameterTypes().size(); i++) {
						if (paramTypes[i].getName().equals(
								(methodConfig.getParameterTypes().get(i)
										.getName()))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param cacheCleanBeans
	 * @return
	 */
	private boolean checkCacheCleanBeanMethodInvalid(
			List<CacheCleanBean> cacheCleanBeans) {
		return true;
	}

	/**
	 * 记录校验出错的信息
	 * 
	 * @param msg
	 */
	private void logCheckInvalid(String msg) {
		logger.error(msg);
	}
}
