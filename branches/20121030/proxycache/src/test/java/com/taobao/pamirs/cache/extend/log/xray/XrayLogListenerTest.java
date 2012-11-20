package com.taobao.pamirs.cache.extend.log.xray;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Test;

import com.taobao.pamirs.cache.framework.listener.CacheOprator;

/**
 * xray��ʽ
 * 
 * @author xiaocheng 2012-11-19
 */
public class XrayLogListenerTest {

	@Test
	public void testOprate() throws Exception {
		ArrayList<Class<?>> params = new ArrayList<Class<?>>();
		params.add(String.class);
		params.add(int.class);

		XrayLogListener xray = new XrayLogListener("bean123", "name456", params);

		Method getXrayLog = XrayLogListener.class.getDeclaredMethod(
				"getXrayLog", CacheOprator.class, boolean.class, long.class);

		getXrayLog.setAccessible(true);

		Object result = getXrayLog.invoke(xray, CacheOprator.GET, true, 100L);

		assertThat(result.toString(),
				equalTo(",bean123,name456,{String|int},GET,true,100"));
	}

}