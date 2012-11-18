package com.taobao.pamirs.cache.load.verify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanMethod;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;

/**
 * 缓存配置合法性校验
 * 
 * <pre>
 * 	  缓存校验内容：
 * 		1：缓存关键配置数据不能为空
 * 		2：缓存关键配置常量不能乱写[tair/map]
 * 		3：缓存方法是否存在重复配置校验
 * 		4：缓存清理方法是否存在重复配置校验
 * 		5：缓存方法配置在Spring中的需要存在，并且合法
 * 		6：缓存清理方法在Spring中需要存在，并且合法
 * </pre>
 * 
 * @author poxiao.gj
 * @date 2012-11-18
 */
public class CacheConfigVerify extends AbstractCacheConfigVerify {

	public static final String STORE_TAIR = "tair";
	public static final String STORE_MAP = "map";

	/**
	 * 缓存校验构造函数
	 * 
	 * @param applicationContext
	 */
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
		if (!checkCacheConfigConstants(cacheConfig)) {
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
	 * 缓存配置厂里的合法性
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkCacheConfigConstants(CacheConfig cacheConfig) {
		if (!(STORE_TAIR.equals(cacheConfig.getStoreType()) || STORE_MAP
				.equals(cacheConfig.getStoreType()))) {
			logCheckInvalid("缓存配置类型必须为tair或者map");
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
		if (!checkCacheNotEmpty(cacheConfig.getCacheBeans())) {
			return false;
		}
		if (cacheConfig.getCacheCleanBeans() == null
				|| cacheConfig.getCacheCleanBeans().isEmpty()) {
			logCheckInvalid("缓存配置数据CacheCleanBeans为空");
			return false;
		}
		if (!checkCleanCacheNotEmpty(cacheConfig.getCacheCleanBeans())) {
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
				String key = getBeanMethodKey(cacheBean.getBeanName(),
						methodConfig.getMethodName(),
						methodConfig.getParameterTypes());
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
				String key = getBeanMethodKey(cacheCleanBean.getBeanName(),
						cacheCleanMethod.getMethodName(),
						cacheCleanMethod.getParameterTypes());
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
	 * 判断缓存对象列表是否合法
	 * 
	 * <pre>
	 * 	校验：
	 * 		1: 在Spring中，缓存的Bean是否存在。
	 * 		2：缓存对象方法是否的确存在。
	 * </pre>
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkCacheBeanMethodInvalid(List<CacheBean> cacheBeans) {
		for (CacheBean cacheBean : cacheBeans) {
			Object bean = getBean(cacheBean.getBeanName());
			if (bean == null) {
				logCheckInvalid("在Spring中查询不到对应的Bean，BeanName="
						+ cacheBean.getBeanName());
				return false;
			}
			for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
				if (methodConfig.getParameterTypes() == null
						|| methodConfig.getParameterTypes().size() <= 0) {
					// 缓存方法的参数不能为空
					logCheckInvalid("缓存对象方法：" + cacheBean.getBeanName() + "."
							+ methodConfig.getMethodName() + "的缓存参数为空");
					return false;
				}
				// 判断当前缓存方法是否合法
				if (!isBeanMethodValid(bean.getClass(), cacheBean.getBeanName(),
						methodConfig.getMethodName(),
						methodConfig.getParameterTypes())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 判断缓存清理列表方法是否合法
	 * 
	 * <pre>
	 * 校验：
	 * 		1: 在Spring中，缓存清理的Bean是否存在。
	 * 		2：缓存清理对象方法是否的确存在。
	 * </pre>
	 * 
	 * @param cacheCleanBeans
	 * @return
	 */
	private boolean checkCacheCleanBeanMethodInvalid(
			List<CacheCleanBean> cacheCleanBeans) {
		for (CacheCleanBean cacheCleanBean : cacheCleanBeans) {
			Object bean = getBean(cacheCleanBean.getBeanName());
			if (bean == null) {
				logCheckInvalid("在Spring中查询不到对应的Bean，BeanName="
						+ cacheCleanBean.getBeanName());
				return false;
			}
			for (CacheCleanMethod cacheCleanMethod : cacheCleanBean
					.getMethods()) {
				if (cacheCleanMethod.getParameterTypes() == null
						|| cacheCleanMethod.getParameterTypes().size() <= 0) {
					// 缓存清理方法的参数不能为空
					logCheckInvalid("缓存对象方法：" + cacheCleanBean.getBeanName()
							+ "." + cacheCleanMethod.getMethodName()
							+ "的缓存参数为空");
					return false;
				}
				// 判断当前缓存清理方法是否合法
				if (!isBeanMethodValid(bean.getClass(), cacheCleanBean.getBeanName(),
						cacheCleanMethod.getMethodName(),
						cacheCleanMethod.getParameterTypes())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 获取错误信息
	 * 
	 * @return
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
}
