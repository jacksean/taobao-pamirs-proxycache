package com.taobao.pamirs.cache.jmx;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.taobao.pamirs.cache.ApplicationContextUtil;
import com.taobao.pamirs.cache.framework.CacheProxy;
import com.taobao.pamirs.cache.framework.aop.advice.CacheManagerRoundAdvice;
import com.taobao.pamirs.cache.jmx.dynamicmbean.AbstractDynamicMBean;

public class CacheMbean<K extends Serializable, V extends Serializable> extends
		AbstractDynamicMBean {
	private static Log log = LogFactory.getLog(CacheMbean.class);

	private CacheProxy<K, V> cache = null;
	private CacheMbeanListener listener;

	private ApplicationContext applicationContext;

	public CacheMbean(CacheProxy<K, V> cache, CacheMbeanListener listener, ApplicationContext applicationContext) {
		this.cache = cache;
		this.listener = listener;
		this.applicationContext = applicationContext;
	}

	public String getCacheName() {
		return cache.getKey();
	}

	public String getStoreType() {
		return cache.getStoreType().getName();
	}
	
	public String getStoreCount() {
		try {
			return cache.size() + "";
		} catch (Exception e) {
			return e.getMessage();
		}
	}
	
	public boolean getIsUseCache() {
		return cache.isUseCache();
	}
	
	public long getReadHits() {
		return listener.getReadSuccessCount().get();
	}

	public long getReadUnHits() {
		return listener.getReadFailCount().get();
	}

	public String getReadHitRate() {
		return listener.getReadHitRate();
	}

	public long getReadAvgTime() {
		return listener.getReadAvgTime();
	}

	public long getWriteAvgTime() {
		return listener.getWriteAvgTime();
	}

	public long getRemoveCount() {
		return listener.getRemoveCount().get();
	}

	public long getExpireTime() {
		return cache.getExpireTimes();
	}

	public String getCleanTime() {
		return cache.getCleanTime();
	}

	public String remove(K key) {
		try {
			cache.remove(key);
		} catch (Exception e) {
			return "Cache Remove Failure Key:" + key;
		}
		
		return "Cache Remove Successfully Key:" + key;
	}

	public V get(K key) {
		return cache.get(key);
	}

	/**
	 * �������ͨ�� Cache ��ȡ��ʵֵ.
	 * **/

	@SuppressWarnings("unchecked")
	public V getRealValue(K key) {

		V result = null;

		try {
			BeanCacheConfig cacheConfig = this.cache.getBeanCacheConfig();
			Object bean = ApplicationContextUtil.getBean(cacheConfig
					.getBeanName());

			String paramterType = cacheConfig.getParameterTypes();
			String[] keyItems = key.toString().split(
					CacheManagerRoundAdvice.VALUE_KEY_SPLITE_SIGN);

			int start = paramterType.indexOf("{");
			int end = paramterType.indexOf("}");

			String subParameterTypes = paramterType.substring(start + 1, end);
			String[] parameterTypesArray = subParameterTypes.split(",");
			if (parameterTypesArray.length != keyItems.length) {
				result = (V) ("jmx�Ĳ��������ͽӿڵĲ���������һ��,����" + key.toString()
						+ "�ӿڲ���:" + paramterType);
				return result;
			}
			Class<?>[] methodParameter = new Class[parameterTypesArray.length];

			Object[] methodArgs = new Object[parameterTypesArray.length];
			for (int i = 0; i < parameterTypesArray.length; i++) {
				if (parameterTypesArray[i].equals("long")) {
					methodParameter[i] = long.class;
					methodArgs[i] = new Long(keyItems[i]).longValue();
				} else if (parameterTypesArray[i].equals("Long")) {
					methodParameter[i] = Long.class;
					methodArgs[i] = new Long(keyItems[i]);
				} else if (parameterTypesArray[i].equals("int")) {
					methodParameter[i] = int.class;
					methodArgs[i] = new Integer(keyItems[i]).intValue();
				} else if (parameterTypesArray[i].equals("Integer")) {
					methodParameter[i] = Integer.class;
					methodArgs[i] = new Integer(keyItems[i]);
				} else if (parameterTypesArray[i].equals("short")) {
					methodParameter[i] = short.class;
					methodArgs[i] = new Short(keyItems[i]).shortValue();
				} else if (parameterTypesArray[i].equals("Short")) {
					methodParameter[i] = Short.class;
					methodArgs[i] = new Short(keyItems[i]);
				} else if (parameterTypesArray[i].equals("String")) {
					methodParameter[i] = String.class;
					methodArgs[i] = new String(keyItems[i]);
				}
			}

			Class<?> beanClass = bean.getClass();
			Method beanMethod = beanClass.getMethod(
					cacheConfig.getMethodName(), methodParameter);

			result = (V) beanMethod.invoke(bean, methodArgs);

		} catch (Exception e) {
			log.error("getRealValue Error :" + e.getMessage(), e);
		}

		return result;
	}

	public String put(K key, V value) {
		try {
			this.cache.put(key, value);
		} catch (Exception e) {
			return "Cache Put Failure Key:" + key + " Value:" + value;
		}
		return "Cache Put Successfully Key:" + key + " Value:" + value;
	}

	protected void buildDynamicMBeanInfo() {
		MBeanAttributeInfo[] dAttributes = new MBeanAttributeInfo[] {
				new MBeanAttributeInfo("cacheName", "String", "��������", true,
						false, false),
				new MBeanAttributeInfo("storeType", "String", "��������", true,
						false, false),
				new MBeanAttributeInfo("storeCount", "String", "����������", true,
						false, false),
				new MBeanAttributeInfo("isUseCache", "boolean", "�Ƿ�ʹ�û���", true,
						false, false),
				new MBeanAttributeInfo("readHits", "long", "�����д���", true,
						false, false),
				new MBeanAttributeInfo("readUnHits", "long", "��δ���д���", true,
						false, false),
				new MBeanAttributeInfo("readHitRate", "double", "����������", true,
						false, false),
				new MBeanAttributeInfo("readAvgTime", "long", "ƽ���������ʱ", true,
						false, false),
				new MBeanAttributeInfo("writeAvgTime", "long", "ƽ������д��ʱ", true,
						false, false),
				new MBeanAttributeInfo("removeCount", "long", "����ɾ������", true,
						false, false),
				new MBeanAttributeInfo("expireTime", "long", "��������ʧЧʱ��", true,
						false, false),
				new MBeanAttributeInfo("cleanTime", "String", "��������ʱ��", true,
						false, false) };

		MBeanOperationInfo[] dOperations = new MBeanOperationInfo[] {
				new MBeanOperationInfo("remove", "����ɾ��",
						new MBeanParameterInfo[] { new MBeanParameterInfo(
								"CacheRemove", "java.lang.String",
								"����String Key.�������@@�ָ�. ���л���� remove ����.") }, "String",
						MBeanOperationInfo.ACTION),
				new MBeanOperationInfo(
						"put",
						"��������д��",
						new MBeanParameterInfo[] {
								new MBeanParameterInfo("CachePut Key",
										"java.lang.String",
										"����String Key.�������@@�ָ�."),
								new MBeanParameterInfo("CachePut Value",
										"java.lang.String", "����String Value.") },
						"String", MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("get", "�������ݶ�ȡ",
						new MBeanParameterInfo[] { new MBeanParameterInfo(
								"CacheGet", "java.lang.String",
								"����String Key.�������@@�ָ�. ���л���� get ����.�������@@�ָ�.") },
						"String", MBeanOperationInfo.ACTION),
				new MBeanOperationInfo("getRealValue", "���������ݶ�ȡ",
						new MBeanParameterInfo[] { new MBeanParameterInfo(
								"DiskGet", "java.lang.String",
								"����String Key.�������@@�ָ�. ����Disk�� get ����.�������@@�ָ�.") },
						"String", MBeanOperationInfo.ACTION) };
		dMBeanInfo = new MBeanInfo(this.getClass().getName(), "Pamirs-Cache",
				dAttributes, null, dOperations, null);
	}
}
