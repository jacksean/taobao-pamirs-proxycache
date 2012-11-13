package com.taobao.pamirs.cache.log.asynlog;

import org.junit.Test;

public class AsynWriterTest {

	@Test
	public void testAsynWriter() throws InterruptedException {
		AsynWriter<String> s = new AsynWriter<String>();
		
		for (int i = 0; i < 10000; i++) {
			s.write("abc" + i);
		}
		
		Thread.sleep(100000);
	}

}
