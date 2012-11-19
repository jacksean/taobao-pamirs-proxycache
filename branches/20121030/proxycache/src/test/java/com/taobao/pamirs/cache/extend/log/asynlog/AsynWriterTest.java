package com.taobao.pamirs.cache.extend.log.asynlog;

import org.junit.Test;

import com.taobao.pamirs.cache.extend.log.asynlog.AsynWriter;

/**
 * “Ï≤Ωlogµ•‘™≤‚ ‘
 * 
 * @author xiaocheng 2012-11-19
 */
public class AsynWriterTest {

	@Test
	public void testAsynWriter() throws InterruptedException {
		AsynWriter<String> s = new AsynWriter<String>();
		
		for (int i = 0; i < 8; i++) {
			s.write("abc" + i);
		}
		
		Thread.sleep(3000);
		
		s.write("jeck");
		Thread.sleep(100000);
	}

}
