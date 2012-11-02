package com.taobao.pamirs.cache2.framework.aop.handle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;

import com.taobao.pamirs.cache2.CacheManager;
import com.taobao.pamirs.cache2.framework.aop.advisor.CacheManagerAdvisor;
import com.taobao.pamirs.cache2.util.ConfigUtil;

/**
 * �������������
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
		if (ConfigUtil.isBeanHaveCache(cacheManager.getCacheConfig(), beanName)) {

			log.info("CacheManager start... ProxyBean:" + beanName);
			
			return new CacheManagerAdvisor[] { new CacheManagerAdvisor(
					cacheManager, beanName) };
		}

		return DO_NOT_PROXY;
	}

}