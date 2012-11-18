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
 * ����У����󹫹�
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
	 * У�黺���ڲ�����ǿ�״̬
	 * 
	 * @param cacheBeans
	 * @return
	 */
	protected boolean checkCacheNotEmpty(List<CacheBean> cacheBeans) {
		for (CacheBean cacheBean : cacheBeans) {
			if (StringUtils.isBlank(cacheBean.getBeanName())) {
				logCheckInvalid("���淽��:" + cacheBean.getBeanName() + "����Ϊ��");
				return false;
			}
			if (cacheBean.getCacheMethods() == null
					|| cacheBean.getCacheMethods().size() <= 0) {
				logCheckInvalid("���淽��:" + cacheBean.getBeanName() + "�µĻ����б�Ϊ��");
				return false;
			}
			for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
				if (StringUtils.isBlank(methodConfig.getMethodName())) {
					logCheckInvalid("���淽��:" + cacheBean.getBeanName()
							+ "�´���Ϊ�յ�");
					return false;
				}
				if (methodConfig.getParameterTypes() == null
						|| methodConfig.getParameterTypes().size() <= 0) {
					logCheckInvalid("���淽��:" + cacheBean.getBeanName() + "."
							+ methodConfig.getMethodName() + "�Ĳ����б�Ϊ��");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * У���������ڲ�����ǿ�״̬
	 * 
	 * @param cacheCleanBeans
	 * @return
	 */
	protected boolean checkCleanCacheNotEmpty(
			List<CacheCleanBean> cacheCleanBeans) {
		// ��cacheCleanBean���ݽṹ�п�
		for (CacheCleanBean cacheCleanBean : cacheCleanBeans) {
			if (StringUtils.isBlank(cacheCleanBean.getBeanName())) {
				logCheckInvalid("��������������Ϊ��");
				return false;
			}
			if (cacheCleanBean.getMethods() == null
					|| cacheCleanBean.getMethods().size() <= 0) {
				logCheckInvalid("��������Bean:" + cacheCleanBean.getBeanName()
						+ "���������б�Ϊ��");
				return false;
			}
			for (CacheCleanMethod cacheCleanMethod : cacheCleanBean
					.getMethods()) {
				if (StringUtils.isBlank(cacheCleanMethod.getMethodName())) {
					logCheckInvalid("��������Bean:" + cacheCleanBean.getBeanName()
							+ "�д���������Ϊ�յ�");
					return false;
				}
				if (cacheCleanMethod.getCleanMethods() == null
						|| cacheCleanMethod.getCleanMethods().size() <= 0) {
					logCheckInvalid("����������:" + cacheCleanBean.getBeanName()
							+ "." + cacheCleanMethod.getMethodName()
							+ "���������б�Ϊ��");
					return false;
				}
				for (MethodConfig methodConfig : cacheCleanMethod
						.getCleanMethods()) {
					if (StringUtils.isBlank(methodConfig.getMethodName())) {
						logCheckInvalid("����������:"
								+ cacheCleanBean.getBeanName() + "."
								+ cacheCleanMethod.getMethodName()
								+ "���������б��д��ڿյ�");
						return false;
					}
					if (methodConfig.getParameterTypes() == null
							|| methodConfig.getParameterTypes().size() <= 0) {
						logCheckInvalid("����������:"
								+ cacheCleanBean.getBeanName() + "."
								+ cacheCleanMethod.getMethodName()
								+ "���������б�ķ���+" + methodConfig.getMethodName()
								+ "�Ĳ����б�Ϊ��");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * ��ȡ���淽����Key
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
	 * �жϻ��淽���Ƿ����ڵ�ǰ��Bean��һ����������֧��interface�̳�interface��
	 * 
	 * @param bean
	 * @param methodConfig
	 * @return
	 */
	protected boolean isBeanMethodValid(Class<?> clazz, String beanName,
			String methodName, List<Class<?>> paramTypes) {
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces.length <= 0) {
			logCheckInvalid("�������" + clazz.getSimpleName() + "�Ľӿ��б�Ϊ��");
			return false;
		}
		for (Class<?> interface_ : interfaces) {
			Method[] methods = interface_.getDeclaredMethods();
			// ����ӿڷ����б�Ϊ�գ���continue
			if (methods.length <= 0) {
				continue;
			}
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					Class<?>[] tmpParamTypes = method.getParameterTypes();
					// ���Classͬ������С�ڻ��淽���Ĳ�������continue
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
		logCheckInvalid("������󷽷�:"
				+ getBeanMethodKey(beanName, methodName, paramTypes)
				+ "����spring���Ҳ�����");
		return false;
	}

	/**
	 * ��ȡSpring��Bean
	 * 
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * ��¼У��������Ϣ
	 * 
	 * @param msg
	 */
	protected void logCheckInvalid(String msg) {
		this.errorMsg = msg;
		logger.error(msg);
	}
}
