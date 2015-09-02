package com.taobao.pamirs.cache.extend.lock.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;

import com.taobao.pamirs.cache.extend.lock.DistributedLock;

@SpringApplicationContext({ "/store/tair-store.xml",
		"/extend/lock/lock-tair.xml" })
public class TairDistributedLockTest extends UnitilsJUnit4 {

	@SpringBeanByName
	DistributedLock distributedLock;

	@Test
	public void test() {
		boolean lock = distributedLock.lock(1, "a");
		assertThat(lock, is(true));

		lock = distributedLock.lock(1, "a");
		assertThat(lock, is(false));

		boolean unLock = distributedLock.unlock(1, "a");
		assertThat(unLock, is(true));

		lock = distributedLock.lock(1, "a");
		assertThat(lock, is(true));
	}

}
