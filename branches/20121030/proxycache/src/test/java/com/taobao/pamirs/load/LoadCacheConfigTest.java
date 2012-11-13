package com.taobao.pamirs.load;

import java.util.ArrayList;
import java.util.List;

import org.unitils.UnitilsJUnit4;

import com.taobao.pamirs.cache.framework.config.CacheBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanBean;
import com.taobao.pamirs.cache.framework.config.CacheCleanMethod;
import com.taobao.pamirs.cache.framework.config.CacheModule;
import com.taobao.pamirs.cache.framework.config.MethodConfig;
import com.taobao.pamirs.cache.util.ConfigUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 
 * @author Administrator
 * 
 */
public class LoadCacheConfigTest extends UnitilsJUnit4 {

	public static void main(String[] args) throws Exception {
		XStream xStream = new XStream(new DomDriver());
		xStream.alias("cacheModule", CacheModule.class);
		xStream.alias("cacheBean", CacheBean.class);
		xStream.alias("methodConfig", MethodConfig.class);
		xStream.alias("cacheCleanBean", CacheCleanBean.class);
		xStream.alias("cacheCleanMethod", CacheCleanMethod.class);
		CacheModule cacheModule = new CacheModule();
		List<CacheBean> cacheBeans = new ArrayList<CacheBean>();
		cacheModule.setCacheBeans(cacheBeans);
		// articleReadService
		CacheBean cacheBean = new CacheBean();
		cacheBean.setBeanName("articleReadService");
		List<MethodConfig> cacheMethods = new ArrayList<MethodConfig>();
		cacheMethods.add(generateCacheBean("getArticleById", Long.class));
		cacheMethods.add(generateCacheBean("getArticleByCode", String.class));
		cacheMethods.add(generateCacheBean("getSaleConditions", Long.class));
		cacheBean.setCacheMethods(cacheMethods);
		cacheBeans.add(cacheBean);
		// itemReadService
		cacheBean = new CacheBean();
		cacheBean.setBeanName("itemReadName");
		cacheMethods = new ArrayList<MethodConfig>();
		cacheMethods.add(generateCacheBean("getItemById", Long.class));
		cacheMethods.add(generateCacheBean("getItemByCode", String.class));
		cacheBean.setCacheMethods(cacheMethods);
		cacheBeans.add(cacheBean);
		
		// clean
		CacheCleanBean cacheCleanBean = new CacheCleanBean();
		cacheCleanBean.setBeanName("itemReadService");
		List<CacheCleanMethod> methods = new ArrayList<CacheCleanMethod>();
		cacheMethods = new ArrayList<MethodConfig>();
		cacheMethods.add(generateCacheBeanEx("getItemById"));
		cacheMethods.add(generateCacheBeanEx("getSaleConditions"));
		methods.add(generateCacheCleanMethod("cleanCacheById", cacheMethods,
				Long.class));
		cacheCleanBean.setMethods(methods);
		List<CacheCleanBean> cacheCleanBeans = new ArrayList<CacheCleanBean>();
		cacheCleanBeans.add(cacheCleanBean);
		cacheModule.setCacheCleanBeans(cacheCleanBeans);

		System.out.println(xStream.toXML(cacheModule));
		System.out.println(ConfigUtil.getCacheConfigModule(xStream
				.toXML(cacheModule)));
		// System.out.println(ToStringBuilder.reflectionToString(
		// getCacheConfigModule(xStream.toXML(configModule)),
		// ToStringStyle.MULTI_LINE_STYLE));
	}

	public static MethodConfig generateCacheBean(String methodName,
			Class<?>... classes) {
		MethodConfig methodConfig = new MethodConfig();
		methodConfig.setMethodName(methodName);
		List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Class<?> class_ : classes) {
			parameterTypes.add(class_);
		}
		methodConfig.setParameterTypes(parameterTypes);
		return methodConfig;
	}

	public static MethodConfig generateCacheBeanEx(String methodName) {
		MethodConfig methodConfig = new MethodConfig();
		methodConfig.setMethodName(methodName);
		return methodConfig;
	}

	public static CacheCleanMethod generateCacheCleanMethod(String methdName,
			List<MethodConfig> methodConfigs, Class<?>... classes) {
		CacheCleanMethod cacheCleanMethod = new CacheCleanMethod();
		cacheCleanMethod.setMethodName(methdName);
		List<Class<?>> parameterTypes = new ArrayList<Class<?>>();
		for (Class<?> class_ : classes) {
			parameterTypes.add(class_);
		}
		cacheCleanMethod.setParameterTypes(parameterTypes);
		cacheCleanMethod.setCleanMethods(methodConfigs);
		return cacheCleanMethod;
	}
}
