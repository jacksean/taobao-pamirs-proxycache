package com.taobao.pamirs.load;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.taobao.pamirs.cache.framework.config.CacheConfig;
import com.taobao.pamirs.cache.load.ICacheConfigService;

/**
 * 
 * @author Administrator
 * 
 */
@SpringApplicationContext({ "load-cache-config.xml" })
public class LoadCacheConfigTest extends UnitilsJUnit4 {

	@SpringBeanByName
	private ICacheConfigService cacheConfigService;

	@Test
	public void testGetConfig() throws Exception {
		CacheConfig cacheConfig = cacheConfigService.loadConfig();
		System.out.println(ToStringBuilder.reflectionToString(cacheConfig));
	}

}
