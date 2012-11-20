package com.taobao.pamirs.cache.load.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.taobao.pamirs.cache.CacheManager;

@SpringApplicationContext({ "/store/tair-store.xml", "/load/cache-spring.xml",
		"/load/jmx-spring.xml" })
public class LocalConfigServiceImplTest extends UnitilsJUnit4 {

	@SpringBeanByName
	private CacheManager cacheManager;

	@Test
	public void test() {
		try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
