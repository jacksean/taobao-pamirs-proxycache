package com.taobao.pamirs.cache2.framework.listener;

import java.io.Serializable;

import com.taobao.pamirs.cache2.framework.CacheException;
import com.taobao.pamirs.cache2.framework.config.MethodConfig;

/**
 * 缓存操作的相关信息
 * 
 * @author xiaocheng 2012-10-31
 */
public class CacheInfo extends MethodConfig implements Serializable {

	//
	private static final long serialVersionUID = 7100282651039776916L;

	private Serializable key;
	private long methodTime;
	private CacheException cacheException;

	public CacheInfo(Serializable key, long methodTime,
			MethodConfig methodConfig, CacheException exception) {
		this.key = key;
		this.methodTime = methodTime;
		this.cacheException = exception;

		if (methodConfig != null) {
			this.setBeanName(methodConfig.getBeanName());
			this.setMethodName(methodConfig.getMethodName());
			this.setParameterTypes(methodConfig.getParameterTypes());
		}
	}

	/**
	 * 缓存操作是否成功
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
