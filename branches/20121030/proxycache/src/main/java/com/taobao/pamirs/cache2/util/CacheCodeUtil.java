package com.taobao.pamirs.cache2.util;

import java.util.List;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.Assert;

import com.taobao.pamirs.cache2.framework.config.MethodConfig;

/**
 * 缓存Code辅助类
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheCodeUtil {

	/**
	 * 取得最终的缓存Code
	 * 
	 * @param region
	 * @param methodConfig
	 * @param invocation
	 * @return
	 */
	public static String getCacheCode(String region, MethodConfig methodConfig,
			MethodInvocation invocation) {
		Assert.notNull(invocation);

		// 最终的缓存code
		StringBuilder code = new StringBuilder();

		// 1. region
		// 2. bean + method + parameter
		code.append(getCacheAdapterKey(region, methodConfig));

		// 3. value
		Object[] parameters = invocation.getArguments();
		StringBuilder valus = new StringBuilder();
		for (Object param : parameters) {
			if (valus.length() != 0) {
				valus.append("@@");
			}

			valus.append(param == null ? "null" : param.toString());
		}
		code.append(valus.toString());

		return code.toString();
	}

	/**
	 * 缓存适配器的key
	 * 
	 * @param region
	 * @param methodConfig
	 * @return
	 */
	public static String getCacheAdapterKey(String region,
			MethodConfig methodConfig) {
		Assert.notNull(methodConfig);

		// 最终的key
		StringBuilder key = new StringBuilder();

		// 1. region
		key.append(region);

		// 2. bean + method + parameter
		String beanName = methodConfig.getBeanName();
		String methodName = methodConfig.getMethodName();
		List<Class<?>> parameterTypes = methodConfig.getParameterTypes();

		StringBuilder parameter = new StringBuilder("{");
		if (parameterTypes != null) {
			for (Class<?> clazz : parameterTypes) {
				if (parameter.length() != 1) {
					parameter.append(",");
				}

				parameter.append(clazz.getSimpleName());
			}
		}
		parameter.append("}");

		key.append(beanName).append("#");
		key.append(methodName).append("#");
		key.append(parameter.toString());

		return key.toString();

	}

}
