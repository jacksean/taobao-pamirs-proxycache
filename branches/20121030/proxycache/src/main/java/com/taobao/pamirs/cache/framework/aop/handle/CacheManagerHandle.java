package com.taobao.pamirs.cache.framework.aop.handle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

import com.taobao.pamirs.cache.CacheManager;
import com.taobao.pamirs.cache.framework.aop.advisor.CacheManagerAdvisor;

/**
 * 缓存管理处理类
 * 
 * @author xuannan
 */
@SuppressWarnings("serial")
public class CacheManagerHandle extends AbstractAutoProxyCreator {

	private static final Log log = LogFactory.getLog(CacheManagerHandle.class);

	private CacheManager cacheManager;

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public CacheManagerHandle() {
		this.setProxyTargetClass(true);
	}

	@SuppressWarnings("rawtypes")
	protected Object[] getAdvicesAndAdvisorsForBean(Class beanClass,
			String beanName, TargetSource targetSource) throws BeansException {

		// 装载 bean 的时候进行缓存代理功能设置.
		if (this.cacheManager.getCacheBeanNameSet().contains(beanName)) {

			log.info("CacheManager ProxyBean:" + beanName);

			return new CacheManagerAdvisor[] { new CacheManagerAdvisor(
					this.cacheManager, beanName) };
		}

		return DO_NOT_PROXY;
	}

}
