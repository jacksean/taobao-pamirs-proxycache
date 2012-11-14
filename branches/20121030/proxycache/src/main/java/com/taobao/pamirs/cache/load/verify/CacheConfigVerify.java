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
 * �������úϷ���У��
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
	 * ���ڻ������ý���ǿУ��
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
	 * У���Ҫ�������ݲ���Ϊ��
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkConfigNotEmpty(CacheConfig cacheConfig) {
		if (cacheConfig == null) {
			logCheckInvalid("������������CacheConfigΪ��");
			return false;
		}
		if (StringUtils.isBlank(cacheConfig.getStoreRegion())
				|| StringUtils.isBlank(cacheConfig.getStoreType())) {
			logCheckInvalid("������������StoreRegion����StoreTypeΪ��");
			return false;
		}
		if (cacheConfig.getCacheBeans() == null
				|| cacheConfig.getCacheBeans().isEmpty()) {
			logCheckInvalid("������������CacheBeansΪ��");
			return false;
		}
		if (cacheConfig.getCacheCleanBeans() == null
				|| cacheConfig.getCacheCleanBeans().isEmpty()) {
			logCheckInvalid("������������CacheCleanBeansΪ��");
			return false;
		}
		return true;
	}

	/**
	 * �жϻ��������Ƿ����ظ�����
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
					logCheckInvalid("�������" + key + "�ظ�����");
					return false;
				}
				dup.put(key, Boolean.TRUE);
			}
		}
		return true;
	}

	/**
	 * �ж��������Ƿ����ظ�����
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
					logCheckInvalid("���������" + key + "�ظ�����");
					return false;
				}
				dup.put(key, Boolean.TRUE);
			}

		}
		return true;
	}

	/**
	 * �жϻ����bean��spring���Ƿ����
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
						// ���淽���Ĳ�������Ϊ��
						return false;
					}
					
				}
			}
		}
		return true;
	}

	/**
	 * �ж�
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
	 * ��¼У��������Ϣ
	 * 
	 * @param msg
	 */
	private void logCheckInvalid(String msg) {
		logger.error(msg);
	}
}
