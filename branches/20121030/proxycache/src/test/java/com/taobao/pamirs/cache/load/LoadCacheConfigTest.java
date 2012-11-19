package com.taobao.pamirs.cache.load;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.taobao.pamirs.cache.CacheManager;
import com.taobao.pamirs.cache.framework.config.CacheConfig;

/**
 * 
 * @author Administrator
 * 
 */
@SpringApplicationContext({ "/load/cache-spring.xml" })
public class LoadCacheConfigTest extends UnitilsJUnit4 {

	@SpringBeanByName
	private CacheManager cacheManager;

	@Test
	public void testGetConfig() throws Exception {
		CacheConfig cacheConfig = cacheManager.loadConfig();
		System.out.println(ToStringBuilder.reflectionToString(cacheConfig));
	}
}
