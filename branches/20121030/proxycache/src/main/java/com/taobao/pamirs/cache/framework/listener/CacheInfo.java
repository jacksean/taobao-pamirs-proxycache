package com.taobao.pamirs.cache.framework.listener;

import java.io.Serializable;

import com.taobao.pamirs.cache.framework.CacheException;
import com.taobao.pamirs.cache.framework.config.MethodConfig;

/**
 * ��������������Ϣ
 * 
 * @author xiaocheng 2012-10-31
 */
public class CacheInfo extends MethodConfig implements Serializable {

	//
	private static final long serialVersionUID = 7100282651039776916L;

	private Serializable key;
	private long methodTime;
	/** �Ƿ����У�for GET�� */
	private boolean isHitting;
	private CacheException cacheException;

	public CacheInfo(Serializable key, long methodTime, boolean isHitting,
			MethodConfig methodConfig, CacheException exception) {
		this.key = key;
		this.methodTime = methodTime;
		this.isHitting = isHitting;
		this.cacheException = exception;

		if (methodConfig != null) {
			this.setBeanName(methodConfig.getBeanName());
			this.setMethodName(methodConfig.getMethodName());
			this.setParameterTypes(methodConfig.getParameterTypes());
		}
	}

	/**
	 * ��������Ƿ�ɹ�
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return cacheException == null;
	}

	public Serializable getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isHitting() {
		return isHitting;
	}

	public long getMethodTime() {
		return methodTime;
	}

	public void setMethodTime(long methodTime) {
		this.methodTime = methodTime;
	}

	public CacheException getCacheException() {
		return cacheException;
	}

}
