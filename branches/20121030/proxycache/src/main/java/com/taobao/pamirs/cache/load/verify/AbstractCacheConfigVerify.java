package com.taobao.pamirs.cache.load.verify;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanMethod;
import com.taobao.pamirs.cache.framework.config.MethodConfig;

/**
 * 缓存校验抽象公共
 * 
 * @author poxiao.gj
 * 
 */
public abstract class AbstractCacheConfigVerify {

	public static final Logger logger = Logger
			.getLogger(CacheConfigVerify.class);
	protected String errorMsg;
	protected ApplicationContext applicationContext;

	/**
	 * 校验缓存内部对象非空状态
	 * 
	 * @param cacheBeans
	 * @return
	 */
	protected boolean checkCacheNotEmpty(List<CacheBean> cacheBeans) {
		for (CacheBean cacheBean : cacheBeans) {
			if (StringUtils.isBlank(cacheBean.getBeanName())) {
				logCheckInvalid("缓存方法:" + cacheBean.getBeanName() + "对象为空");
				return false;
			}
			if (cacheBean.getCacheMethods() == null
					|| cacheBean.getCacheMethods().size() <= 0) {
				logCheckInvalid("缓存方法:" + cacheBean.getBeanName() + "下的缓存列表为空");
				return false;
			}
			for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
				if (StringUtils.isBlank(methodConfig.getMethodName())) {
					logCheckInvalid("缓存方法:" + cacheBean.getBeanName()
							+ "下存在为空的");
					return false;
				}
				if (methodConfig.getParameterTypes() == null
						|| methodConfig.getParameterTypes().size() <= 0) {
					logCheckInvalid("缓存方法:" + cacheBean.getBeanName() + "."
							+ methodConfig.getMethodName() + "的参数列表为空");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 校验清理缓存内部对象非空状态
	 * 
	 * @param cacheCleanBeans
	 * @return
	 */
	protected boolean checkCleanCacheNotEmpty(
			List<CacheCleanBean> cacheCleanBeans) {
		// 对cacheCleanBean数据结构判空
		for (CacheCleanBean cacheCleanBean : cacheCleanBeans) {
			if (StringUtils.isBlank(cacheCleanBean.getBeanName())) {
				logCheckInvalid("缓存清理方法不能为空");
				return false;
			}
			if (cacheCleanBean.getMethods() == null
					|| cacheCleanBean.getMethods().size() <= 0) {
				logCheckInvalid("缓存清理Bean:" + cacheCleanBean.getBeanName()
						+ "的清理方法列表为空");
				return false;
			}
			for (CacheCleanMethod cacheCleanMethod : cacheCleanBean
					.getMethods()) {
				if (StringUtils.isBlank(cacheCleanMethod.getMethodName())) {
					logCheckInvalid("缓存清理Bean:" + cacheCleanBean.getBeanName()
							+ "中存在清理方法为空的");
					return false;
				}
				if (cacheCleanMethod.getCleanMethods() == null
						|| cacheCleanMethod.getCleanMethods().size() <= 0) {
					logCheckInvalid("缓存清理方法:" + cacheCleanBean.getBeanName()
							+ "." + cacheCleanMethod.getMethodName()
							+ "中清理缓存列表为空");
					return false;
				}
				for (MethodConfig methodConfig : cacheCleanMethod
						.getCleanMethods()) {
					if (StringUtils.isBlank(methodConfig.getMethodName())) {
						logCheckInvalid("缓存清理方法:"
								+ cacheCleanBean.getBeanName() + "."
								+ cacheCleanMethod.getMethodName()
								+ "中清理缓存列表中存在空的");
						return false;
					}
					if (methodConfig.getParameterTypes() == null
							|| methodConfig.getParameterTypes().size() <= 0) {
						logCheckInvalid("缓存清理方法:"
								+ cacheCleanBean.getBeanName() + "."
								+ cacheCleanMethod.getMethodName()
								+ "中清理缓存列表的方法+" + methodConfig.getMethodName()
								+ "的参数列表为空");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * 获取缓存方法的Key
	 * 
	 * @param beanName
	 * @param methodConfig
	 * @return
	 */
	protected String getBeanMethodKey(String beanName, String methodName,
			List<Class<?>> paramTypes) {
		String key = beanName + "." + methodName;
		for (Class<?> paramType : paramTypes) {
			key += "#{" + paramType.getSimpleName() + "},";
		}
		key = key.substring(0, key.length() - 1);
		return key;
	}

	/**
	 * 判断缓存方法是否属于当前的Bean的一个方法【不支持interface继承interface】
	 * 
	 * @param bean
	 * @param methodConfig
	 * @return
	 */
	protected boolean isBeanMethodValid(Class<?> clazz, String beanName,
			String methodName, List<Class<?>> paramTypes) {
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length <= 0) {
			logCheckInvalid("缓存对象：" + clazz.getSimpleName() + "的接口列表为空");
			return false;
		}
		for (Class<?> interface_ : interfaces) {
			Method[] methods = interface_.getDeclaredMethods();
			// 如果接口方法列表为空，则continue
			if (methods.length <= 0) {
				continue;
			}
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					Class<?>[] tmpParamTypes = method.getParameterTypes();
					// 如果Class同名参数小于缓存方法的参数，则continue
					if (tmpParamTypes.length < paramTypes.size()) {
						continue;
					}
					boolean flag = true;
					for (int i = 0; i < paramTypes.size(); i++) {
						if (!tmpParamTypes[i].equals(paramTypes.get(i))) {
							flag = false;
						}
					}
					if (flag) {
						return true;
					}
				}
			}
		}
		logCheckInvalid("缓存对象方法:"
				+ getBeanMethodKey(beanName, methodName, paramTypes)
				+ "，在spring中找不到。");
		return false;
	}

	/**
	 * 获取Spring中Bean
	 * 
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * 记录校验出错的信息
	 * 
	 * @param msg
	 */
	protected void logCheckInvalid(String msg) {
		this.errorMsg = msg;
		logger.error(msg);
	}
}
