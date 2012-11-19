package com.taobao.pamirs.cache.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.InputStream;
import java.util.List;

import org.junit.Test;

import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanMethod;
import com.taobao.pamirs.cache.framework.config.CacheModule;
import com.taobao.pamirs.cache.framework.config.MethodConfig;

/**
 * 配置辅助类单元测试
 * 
 * @author xiaocheng 2012-11-19
 */
public class ConfigUtilTest {

	@Test
	public void testIsBeanHaveCache() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCacheMethod() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCacheCleanMethods() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCacheConfigModule() throws Exception {
		InputStream configStream = ConfigUtilTest.class.getClassLoader()
				.getResourceAsStream("load/cache/cache-config.xml");
		CacheModule cacheModule = ConfigUtil.getCacheConfigModule(configStream);

		assertThat(cacheModule.getCacheBeans().size(), equalTo(2));
		assertThat(cacheModule.getCacheCleanBeans().size(), equalTo(1));

		// 1. cache bean
		for (CacheBean cacheBean : cacheModule.getCacheBeans()) {

			if (cacheBean.getBeanName().equals("aService")) {
				assertThat(cacheBean.getCacheMethods().size(), equalTo(4));

				for (MethodConfig methodConfig : cacheBean.getCacheMethods()) {
					if (methodConfig.getMethodName().equals("md5Name")) {// 两个

						if (methodConfig.getParameterTypes().size() == 1) {
							assertThat(methodConfig.getParameterTypes().get(0)
									.getSimpleName(), equalTo("String"));
						} else {
							assertThat(methodConfig.getParameterTypes().size(),
									equalTo(2));
							assertThat(methodConfig.getParameterTypes().get(0)
									.getSimpleName(), equalTo("String"));
							assertThat(methodConfig.getParameterTypes().get(1)
									.getSimpleName(), equalTo("String"));
						}

						assertThat(methodConfig.getExpiredTime(), nullValue());
					}

					if (methodConfig.getMethodName().equals("firstHaveValue")) {
						assertThat(methodConfig.getExpiredTime(), equalTo(2));
						assertThat(methodConfig.getParameterTypes().size(),
								equalTo(1));
						assertThat(methodConfig.getParameterTypes().get(0)
								.getSimpleName(), equalTo("String"));
					}

					if (methodConfig.getMethodName().equals("noRewirteMethod")) {
						assertThat(methodConfig.getExpiredTime(), nullValue());
						assertThat(methodConfig.getParameterTypes(),
								nullValue());
					}

				}

			} else {
				assertThat(cacheBean.getCacheMethods().size(), equalTo(1));
				MethodConfig methodConfig = cacheBean.getCacheMethods().get(0);
				assertThat(methodConfig.getMethodName(),
						equalTo("doVarietyArgs"));
				assertThat(methodConfig.getExpiredTime(), nullValue());
				assertThat(methodConfig.getParameterTypes().size(), equalTo(17));

				List<Class<?>> parameterTypes = methodConfig
						.getParameterTypes();
				assertThat(parameterTypes.get(0).getSimpleName(),
						equalTo("boolean"));
				assertThat(parameterTypes.get(1).getSimpleName(),
						equalTo("Boolean"));
				assertThat(parameterTypes.get(2).getSimpleName(),
						equalTo("char"));
				assertThat(parameterTypes.get(3).getSimpleName(),
						equalTo("Character"));
				assertThat(parameterTypes.get(4).getSimpleName(),
						equalTo("byte"));
				assertThat(parameterTypes.get(5).getSimpleName(),
						equalTo("Byte"));
				assertThat(parameterTypes.get(6).getSimpleName(),
						equalTo("short"));
				assertThat(parameterTypes.get(7).getSimpleName(),
						equalTo("Short"));
				assertThat(parameterTypes.get(8).getSimpleName(),
						equalTo("int"));
				assertThat(parameterTypes.get(9).getSimpleName(),
						equalTo("Integer"));
				assertThat(parameterTypes.get(10).getSimpleName(),
						equalTo("long"));
				assertThat(parameterTypes.get(11).getSimpleName(),
						equalTo("Long"));
				assertThat(parameterTypes.get(12).getSimpleName(),
						equalTo("float"));
				assertThat(parameterTypes.get(13).getSimpleName(),
						equalTo("Float"));
				assertThat(parameterTypes.get(14).getSimpleName(),
						equalTo("double"));
				assertThat(parameterTypes.get(15).getSimpleName(),
						equalTo("Double"));
				assertThat(parameterTypes.get(16).getSimpleName(),
						equalTo("Date"));
			}

		}

		// 2. clear cache bean
		CacheCleanBean cacheCleanBean = cacheModule.getCacheCleanBeans().get(0);
		assertThat(cacheCleanBean.getBeanName(), equalTo("aService"));
		assertThat(cacheCleanBean.getMethods().size(), equalTo(1));

		// 2.1 methods
		CacheCleanMethod cacheCleanMethod = cacheCleanBean.getMethods().get(0);
		assertThat(cacheCleanMethod.getMethodName(), equalTo("md5Name"));
		assertThat(cacheCleanMethod.getExpiredTime(), nullValue());
		assertThat(cacheCleanMethod.getParameterTypes().size(), equalTo(2));
		assertThat(cacheCleanMethod.getParameterTypes().get(0).getSimpleName(),
				equalTo("String"));
		assertThat(cacheCleanMethod.getParameterTypes().get(0).getSimpleName(),
				equalTo("String"));

		// 2.2 sub-clear-mothods
		assertThat(cacheCleanMethod.getCleanMethods().size(), equalTo(1));
		MethodConfig clear = cacheCleanMethod.getCleanMethods().get(0);
		assertThat(clear.getMethodName(), equalTo("clearNames"));
		assertThat(clear.getExpiredTime(), nullValue());
		assertThat(clear.getParameterTypes(), nullValue());
	}

}
