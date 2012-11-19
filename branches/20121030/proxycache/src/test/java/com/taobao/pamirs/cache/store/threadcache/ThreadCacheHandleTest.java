package com.taobao.pamirs.cache.store.threadcache;

import java.util.Date;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taobao.pamirs.cache.store.threadcache.beans.BeanHaveInterface;
import com.taobao.pamirs.cache.store.threadcache.beans.BeanNormal;
import com.taobao.pamirs.cache.store.threadcache.beans.BeanVarietyArgs;
import com.taobao.pamirs.cache.store.threadcache.beans.Ibean;

/**
 * test方法没有assert，目前看threadcache.log来人工判断
 * 
 * @author xiaocheng 2012-9-6
 */
public class ThreadCacheHandleTest {

	private static ApplicationContext context;

	private static BeanNormal beanNormal;
	private static BeanHaveInterface beanHaveInterface;
	private static Ibean beanFinal;
	private static BeanVarietyArgs beanVarietyArgs;

	@BeforeClass
	public static void init() {
		context = new ClassPathXmlApplicationContext(
				new String[] { "store/bean-threadcache.xml" });

		beanNormal = (BeanNormal) context.getBean("beanNormal");
		beanHaveInterface = (BeanHaveInterface) context.getBean("beanHaveInterface");
		beanFinal = (Ibean) context.getBean("beanFinal");
		beanVarietyArgs = (BeanVarietyArgs) context.getBean("beanVarietyArgs");
	}

	@Test
	public void testSignleThread() {
		ThreadContext.startLocalCache();
		
		try {
			for (int i = 0; i < 10; i++) {
				beanNormal.sayHello();
				System.out.println("Call: " + beanNormal.getName());
				
				beanHaveInterface.sayHello();
				System.out.println("Call: " + beanHaveInterface.getName());
				
				beanFinal.sayHello();
				System.out.println("Call: " + beanFinal.getName());
				
				
				beanVarietyArgs.sayHelloPrimitive(true, 'a', (byte)1, (short)2, 3, 4L, 5.01F, 6.02D);
				beanVarietyArgs.sayHelloBox(true, 'a', (byte)1, (short)2, 3, 4L, 5.01F, 6.02D, new Date());
				beanVarietyArgs.sayHelloObject(new HashMap<Object, Object>());
			}
			
		} finally {
			ThreadContext.remove();
		}
		
	}
	
	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			
			for (int i = 0; i < 10; i++) {
				beanNormal.sayHello();
				System.out.println(this.getName() + " Call: " + beanNormal.getName());
			}
		}
	}
	
	@Test
	public void testSubMultiThreads() {
		ThreadContext.startLocalCache();
		
		for (int i = 0; i < 5; i++) {
			beanNormal.sayHello();
			System.out.println("Call: " + beanNormal.getName());
		}
		
		MyThread thread1 = new MyThread();
		MyThread thread2 = new MyThread();
		
		thread1.start();
		thread2.start();
		
		ThreadContext.remove();
	}

}
