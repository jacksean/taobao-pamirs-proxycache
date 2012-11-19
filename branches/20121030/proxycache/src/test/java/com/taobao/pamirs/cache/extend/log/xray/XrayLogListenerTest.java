package com.taobao.pamirs.cache.extend.log.xray;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Method;

import org.junit.Test;

import com.taobao.pamirs.cache.framework.listener.CacheOprator;

/**
 * xray∏Ò Ω
 * 
 * @author xiaocheng 2012-11-19
 */
public class XrayLogListenerTest {

	@Test
	public void testOprate() throws Exception {
		XrayLogListener xray = new XrayLogListener("bean123", "name456");

		Method getXrayLog = XrayLogListener.class.getDeclaredMethod(
				"getXrayLog", CacheOprator.class, boolean.class, long.class);

		getXrayLog.setAccessible(true);

		Object result = getXrayLog.invoke(xray, CacheOprator.GET, true, 100L);

		assertThat(result.toString(), equalTo(",bean123,name456,GET,true,100"));
	}

}
