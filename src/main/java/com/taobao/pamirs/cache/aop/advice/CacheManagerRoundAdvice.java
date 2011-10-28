package com.taobao.pamirs.cache.aop.advice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.pamirs.cache.cache.Cache;
import com.taobao.pamirs.cache.config.BeanCacheCleanConfig;
import com.taobao.pamirs.cache.config.BeanCacheConfig;
import com.taobao.pamirs.cache.manager.CacheManager;

public class CacheManagerRoundAdvice implements MethodInterceptor, Advice {
	private static transient Log log = LogFactory.getLog(CacheManagerRoundAdvice.class);
	/**
	 * Map<method,�������>
	 */
	CacheManager cacheManager;
	String beanName;
	
	public CacheManagerRoundAdvice(CacheManager cacheManager,String beanName){
		this.cacheManager = cacheManager;
		this.beanName = beanName;
	}

	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		BeanCacheConfig beanCacheConfig;
		BeanCacheCleanConfig beanCacheCleanConfig;
		String cacheCode ;
		
		try {
			Method m = invocation.getMethod();
			Class<?>[] p = m.getParameterTypes();
			
			//��ȡ cacheMap , cacheCleanMap
			Map<String,BeanCacheConfig> cacheMap = cacheManager.getBeanCacheConfigMap();
			Map<String,BeanCacheCleanConfig> cacheCleanMap = cacheManager.getBeanCacheCleanConfigMap();
				
			cacheCode = BeanCacheConfig.generateCacheCode(this.beanName , m.getName() , p);
			
			beanCacheConfig = cacheMap.get(cacheCode);
			beanCacheCleanConfig = cacheCleanMap.get(cacheCode);
		} catch (Exception e) {		
			log.error(e.getMessage(),e);
			return invocation.proceed();
		}	
			
		//�������Ӹ� try /catch ������Ϊ�����  cache ����.
		//����� invocation.proceed();
		//Ϊ�˱���ִ�ж��   invocation.proceed()
		//�����ڲ����� try/cache . ������ִ��.
		try {
			if(beanCacheConfig != null && cacheManager.isUseCache()){
				return useCache(invocation,cacheCode);
			}else if(beanCacheCleanConfig != null){
				return cleanCache(invocation,beanCacheCleanConfig.getCacheCleanCodes());
			}else{
				return invocation.proceed();
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			throw e;
		}

		

	}
	
	public Object cleanCache(MethodInvocation invocation, String[] cacheCleanCodes) throws Throwable {

		try {
			//ִ�з���
			return invocation.proceed();
		} finally {
			Object[] parameters = invocation.getArguments();
			//����ѡ�õ�һ������. ��Ҫ����û���Ĵ��뷽��. ��һ������Ϊ valueKey
			String valueKey = parameters[0].toString();
			
			//�����ص� Cache Value
			for (int i = 0; i < cacheCleanCodes.length; i++) {
				Cache<String,Object> cache = CacheManager.getCache(cacheCleanCodes[i]);
				if( cache != null  ){
					cache.remove(valueKey);
				}
			}
		}
	}

	public Object useCache(MethodInvocation invocation, String cacheCode) throws Throwable {

		Cache<String,Object> Cache = CacheManager.getCache(cacheCode);
		
		if( Cache == null ){
		    return invocation.proceed();
		}else{

			Object[] parameters = invocation.getArguments();
			//����ѡ�õ�һ������. ��Ҫ����û���Ĵ��뷽��. ��һ������Ϊ valueKey
			String valueKey = parameters[0].toString();
			
			Object result = Cache.get(valueKey);
			if(result == null){
				result = invocation.proceed();
				Cache.put(valueKey, result);
			}
			return result;
		}
	}
	public static Object getProperty(Object bean, Object name) {
		try {
			if(bean.getClass().isArray() && name.equals("length")){
			   return ((Object[])bean).length;
			}else if (bean instanceof Class) {
				Field f = ((Class<?>) bean).getDeclaredField(name.toString());
				return f.get(null);
			}else if(bean instanceof Map ){
				return ((Map<?,?>)bean).get(name);
		    }else {
				Object obj = PropertyUtils.getProperty(bean, name.toString());
				return obj;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}