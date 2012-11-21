package com.taobao.pamirs.cache.extend.jmx;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.taobao.pamirs.cache.extend.jmx.mbean.AbstractDynamicMBean;
import com.taobao.pamirs.cache.framework.CacheProxy;
import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.util.CacheCodeUtil;
import com.taobao.pamirs.cache.util.IpUtil;

/**
 * ����bean��Mbean
 * 
 * @author xuanyu
 * @author xiaocheng 2012-11-8
 */
public class CacheMbean<K extends Serializable, V extends Serializable> extends
		AbstractDynamicMBean {
	private static final Log log = LogFactory.getLog(CacheMbean.class);

	public static final String MBEAN_NAME = "Pamirs-Cache";

	private CacheProxy<K, V> cache = null;
	private CacheMbeanListener listener;
	private ApplicationContext applicationContext;
	/**
	 * ʧЧʱ�䣬��λ���롣
	 * 
	 * @see CacheBean.expiredTime
	 */
	private Integer expiredTime;
	/**
	 * Map�Զ�������ʽ
	 * 
	 * @see CacheConfig.storeMapCleanTime
	 */
	private String storeMapCleanTime;

	public CacheMbean(CacheProxy<K, V> cache, CacheMbeanListener listener,
			ApplicationContext applicationContext, String storeMapCleanTime,
			Integer expiredTime) {
		this.cache = cache;
		this.listener = listener;
		this.applicationContext = applicationContext;
		this.storeMapCleanTime = storeMapCleanTime;
		this.expiredTime = expiredTime;
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
		return expiredTime == null ? 0L : expiredTime.longValue();
	}

	public String getCleanTimeExpression() {
		return storeMapCleanTime;
	}

	public String remove(K key) {
		try {
			cache.remove(key, IpUtil.getLocalIp());
		} catch (Exception e) {
			return "Cache Remove Failure Key:" + key;
		}

		return "Cache Remove Successfully Key:" + key;
	}

	public V get(K key) {
		return cache.get(key, IpUtil.getLocalIp());
	}
	
	/**
	 * �������ͨ�� Cache ��ȡ��ʵֵ.
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public V getRealValue(K key) {

		try {
			MethodConfig methodConfig = cache.getMethodConfig();
			Object bean = applicationContext.getBean(cache.getBeanName());
			List<Class<?>> parameterTypes = methodConfig.getParameterTypes();

			Method method = null;

			// 1. �޲η���
			if (parameterTypes == null || parameterTypes.isEmpty()) {
				method = bean.getClass()
						.getMethod(methodConfig.getMethodName());
				return (V) method.invoke(bean);
			}

			// 2. �вη���
			String[] keyItems = key.toString().split(
					CacheCodeUtil.CODE_PARAM_VALUES_SPLITE_SIGN);

			if (keyItems.length != parameterTypes.size()) {
				String erroMsg = "jmx�Ĳ��������ͽӿڵĲ���������һ��,����" + key.toString()
						+ "�ӿڲ���:" + parameterTypes;
				return (V) erroMsg;
			}

			Object[] parameterValues = new Object[parameterTypes.size()];

			for (int i = 0; i < parameterTypes.size(); i++) {
				Class<?> clz = parameterTypes.get(i);

				if (clz.isAssignableFrom(boolean.class)
						|| clz.isAssignableFrom(Boolean.class)) {
					parameterValues[i] = Boolean.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(char.class)
						|| clz.isAssignableFrom(Character.class)) {
					parameterValues[i] = keyItems[i].toCharArray()[0];
				} else if (clz.isAssignableFrom(byte.class)
						|| clz.isAssignableFrom(Byte.class)) {
					parameterValues[i] = Byte.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(short.class)
						|| clz.isAssignableFrom(Short.class)) {
					parameterValues[i] = Short.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(int.class)
						|| clz.isAssignableFrom(Integer.class)) {
					parameterValues[i] = Integer.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(long.class)
						|| clz.isAssignableFrom(Long.class)) {
					parameterValues[i] = Long.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(float.class)
						|| clz.isAssignableFrom(Float.class)) {
					parameterValues[i] = Float.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(double.class)
						|| clz.isAssignableFrom(Double.class)) {
					parameterValues[i] = Double.valueOf(keyItems[i]);
				} else if (clz.isAssignableFrom(Date.class)) {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					parameterValues[i] = format.parse(keyItems[i]);
				} else if (clz.isAssignableFrom(String.class)) {
					parameterValues[i] = keyItems[i];
				}
			}

			Class<?>[] parameterTypeArray = new Class<?>[parameterTypes.size()];
			for (int i = 0; i < parameterTypeArray.length; i++) {
				parameterTypeArray[i] = parameterTypes.get(i);
			}
			
			method = bean.getClass().getMethod(methodConfig.getMethodName(), parameterTypeArray);
			return (V) method.invoke(bean, parameterValues);
		} catch (Exception e) {
			log.error("getRealValue Error :" + e.getMessage(), e);
			return (V) e.getMessage();
		}
	}

	public String put(K key, V value) {
		try {
			this.cache.put(key, value, IpUtil.getLocalIp());
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
				new MBeanAttributeInfo("cleanTimeExpression", "String", "��������ʱ��", true,
						false, false) };

		MBeanOperationInfo[] dOperations = new MBeanOperationInfo[] {
				new MBeanOperationInfo("remove", "����ɾ��",
						new MBeanParameterInfo[] { new MBeanParameterInfo(
								"CacheRemove", "java.lang.String",
								"����String Key.�������@@�ָ�. ���л���� remove ����.") },
						"String", MBeanOperationInfo.ACTION),
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
				new MBeanOperationInfo(
						"get",
						"�������ݶ�ȡ",
						new MBeanParameterInfo[] { new MBeanParameterInfo(
								"CacheGet", "java.lang.String",
								"����String Key.�������@@�ָ�. ���л���� get ����.�������@@�ָ�.") },
						"String", MBeanOperationInfo.ACTION),
				new MBeanOperationInfo(
						"getRealValue",
						"���������ݶ�ȡ",
						new MBeanParameterInfo[] { new MBeanParameterInfo(
								"DiskGet", "java.lang.String",
								"����String Key.�������@@�ָ�. ����Disk�� get ����.�������@@�ָ�.") },
						"String", MBeanOperationInfo.ACTION) };
		dMBeanInfo = new MBeanInfo(this.getClass().getName(), MBEAN_NAME,
				dAttributes, null, dOperations, null);
	}
}
