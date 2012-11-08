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
	 * Key的主分隔符<br>
	 * 格式：regionbeanName#methodName#{String}
	 */
	public static final String KEY_SPLITE_SIGN = "#";
	/**
	 * key中方法参数的分隔符<br>
	 * 格式：{String,Long}
	 */
	public static final String KEY_PARAMS_SPLITE_SIGN = ",";

	/**
	 * 取得最终的缓存Code中参数值分隔符<br>
	 * 格式：regionbeanName#methodName#{String,Long}abc@@123
	 */
	public static final String CODE_PARAM_VALUES_SPLITE_SIGN = "@@";

	/**
	 * 取得最终的缓存Code<br>
	 * 格式：regionbeanName#methodName#{String,Long}abc@@123
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
				valus.append(CODE_PARAM_VALUES_SPLITE_SIGN);
			}

			valus.append(param == null ? "null" : param.toString());
		}
		code.append(valus.toString());

		return code.toString();
	}

	/**
	 * 缓存适配器的key<br>
	 * 格式：regionbeanName#methodName#{String,Long}
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
					parameter.append(KEY_PARAMS_SPLITE_SIGN);
				}

				parameter.append(clazz.getSimpleName());
			}
		}
		parameter.append("}");

		key.append(beanName).append(KEY_SPLITE_SIGN);
		key.append(methodName).append(KEY_SPLITE_SIGN);
		key.append(parameter.toString());

		return key.toString();

	}

}
