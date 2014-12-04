package com.taobao.pamirs.cache.old;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.store.StoreObject;
import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;

/**
 * ����д�ϵĻ��棬�ȶ���ɾ��
 * @author tiebi.hlw
 *
 */
public class OldCacheManager {
	
	
	private static final Log logger = LogFactory
	.getLog(OldCacheManager.class);
	
	/**
	 * �Ƿ����
	 */
	private boolean compatible;

	
	private TairManager oldTairManager;
	
	
	private String region;
	
	/**
	 * ֻ����Ʒ����
	 */
	private int namespace=318;
	
	
	
	public void put(String beanName,MethodInvocation invocation, Object value) {
		
		try{
			String key=CacheUtils.getTairKey(region, invocation, beanName);
			
			StoreObject<String,Object> co = new StoreObject<String,Object>(value);		
			
			this.remove(key);
			ResultCode rc = oldTairManager.put(namespace, key,
					(Serializable) co, 0, 0);
			if (!ResultCode.SUCCESS.equals(rc)) {
				ResultCode rc1 = oldTairManager.put(namespace, key,
						(Serializable) co, 0, 0);
				if (!ResultCode.SUCCESS.equals(rc1)) {
					logger.error("Tair Cache failed to invalid object [namespace="
							+ namespace + ", key=" + key
							+ "]. Error Message : " + rc1.getMessage());
				}
			}
		}catch(Exception e){
			logger.error("OldCacheManager put error,"+beanName+"."+invocation.getMethod().getName()
					+" arg:"+invocation.getArguments()[0], e);
		}
	}
	
	
	
	public void remove(String beanName,MethodConfig method,Object[] params){
		try{
			String key=CacheUtils.getTairKey(region, beanName, method, params);
			this.remove(key);
		}catch(Exception e){
			logger.error("OldCacheManager put error,"+beanName+"."+method.getMethodName()
					+" arg:"+params[0], e);
		}
		
	}
	
	private void remove(String key) {
		ResultCode rc = oldTairManager.invalid(namespace, key);
		if (!ResultCode.SUCCESS.equals(rc)) {
			ResultCode rc1 = oldTairManager.invalid(namespace, key);
			if (!ResultCode.SUCCESS.equals(rc)) {
				logger.error("Tair Cache failed to invalid object [namespace="
						+ namespace + ", key=" + key
						+ "]. Error Message : " + rc1.getMessage());
			}
		}
	}

	public boolean isCompatible() {
		return compatible;
	}


	public void setCompatible(boolean compatible) {
		this.compatible = compatible;
	}


	public void setOldTairManager(TairManager oldTairManager) {
		this.oldTairManager = oldTairManager;
	}



	public void setRegion(String region) {
		this.region = region;
	}
	
	
	

}
