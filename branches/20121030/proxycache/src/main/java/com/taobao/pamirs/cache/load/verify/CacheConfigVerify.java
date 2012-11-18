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
 * �������úϷ���У��
 * 
 * <pre>
 * 	  ����У�����ݣ�
 * 		1������ؼ��������ݲ���Ϊ��
 * 		2������ؼ����ó���������д[tair/map]
 * 		3�����淽���Ƿ�����ظ�����У��
 * 		4�������������Ƿ�����ظ�����У��
 * 		5�����淽��������Spring�е���Ҫ���ڣ����ҺϷ�
 * 		6��������������Spring����Ҫ���ڣ����ҺϷ�
 * </pre>
 * 
 * @author poxiao.gj
 * @date 2012-11-18
 */
public class CacheConfigVerify extends AbstractCacheConfigVerify {

	public static final String STORE_TAIR = "tair";
	public static final String STORE_MAP = "map";

	/**
	 * ����У�鹹�캯��
	 * 
	 * @param applicationContext
	 */
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
	 * �������ó���ĺϷ���
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkCacheConfigConstants(CacheConfig cacheConfig) {
		if (!(STORE_TAIR.equals(cacheConfig.getStoreType()) || STORE_MAP
				.equals(cacheConfig.getStoreType()))) {
			logCheckInvalid("�����������ͱ���Ϊtair����map");
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
		if (!checkCacheNotEmpty(cacheConfig.getCacheBeans())) {
			return false;
		}
		if (cacheConfig.getCacheCleanBeans() == null
				|| cacheConfig.getCacheCleanBeans().isEmpty()) {
			logCheckInvalid("������������CacheCleanBeansΪ��");
			return false;
		}
		if (!checkCleanCacheNotEmpty(cacheConfig.getCacheCleanBeans())) {
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
				String key = getBeanMethodKey(cacheBean.getBeanName(),
						methodConfig.getMethodName(),
						methodConfig.getParameterTypes());
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
				String key = getBeanMethodKey(cacheCleanBean.getBeanName(),
						cacheCleanMethod.getMethodName(),
						cacheCleanMethod.getParameterTypes());
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
	 * �жϻ�������б��Ƿ�Ϸ�
	 * 
	 * <pre>
	 * 	У�飺
	 * 		1: ��Spring�У������Bean�Ƿ���ڡ�
	 * 		2��������󷽷��Ƿ��ȷ���ڡ�
	 * </pre>
	 * 
	 * @param cacheConfig
	 * @return
	 */
	private boolean checkCacheBeanMethodInvalid(List<CacheBean> cacheBeans) {
		for (CacheBean cacheBean : cacheBeans) {
			Object bean = getBean(cacheBean.getBeanName());
			if (bean == null) {
				logCheckInvalid("��Spring�в�ѯ������Ӧ��Bean��BeanName="
						+ cacheBean.getBeanName());
				return false;
			}
			for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
				if (methodConfig.getParameterTypes() == null
						|| methodConfig.getParameterTypes().size() <= 0) {
					// ���淽���Ĳ�������Ϊ��
					logCheckInvalid("������󷽷���" + cacheBean.getBeanName() + "."
							+ methodConfig.getMethodName() + "�Ļ������Ϊ��");
					return false;
				}
				// �жϵ�ǰ���淽���Ƿ�Ϸ�
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
	 * �жϻ��������б����Ƿ�Ϸ�
	 * 
	 * <pre>
	 * У�飺
	 * 		1: ��Spring�У����������Bean�Ƿ���ڡ�
	 * 		2������������󷽷��Ƿ��ȷ���ڡ�
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
				logCheckInvalid("��Spring�в�ѯ������Ӧ��Bean��BeanName="
						+ cacheCleanBean.getBeanName());
				return false;
			}
			for (CacheCleanMethod cacheCleanMethod : cacheCleanBean
					.getMethods()) {
				if (cacheCleanMethod.getParameterTypes() == null
						|| cacheCleanMethod.getParameterTypes().size() <= 0) {
					// �����������Ĳ�������Ϊ��
					logCheckInvalid("������󷽷���" + cacheCleanBean.getBeanName()
							+ "." + cacheCleanMethod.getMethodName()
							+ "�Ļ������Ϊ��");
					return false;
				}
				// �жϵ�ǰ�����������Ƿ�Ϸ�
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
	 * ��ȡ������Ϣ
	 * 
	 * @return
	 */
	public String getErrorMsg() {
		return errorMsg;
	}
}
