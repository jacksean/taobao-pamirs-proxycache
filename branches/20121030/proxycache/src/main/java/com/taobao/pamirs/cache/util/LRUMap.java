package com.taobao.pamirs.cache.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class LRUMap {

//	static Map<String, String> map1 = new ConcurrentHashMap<String, String>();
//	// static Map<String, String> map2 = new Hashtable<String, String>();
//	static Map<String, String> map2 = Collections
//			.synchronizedMap(new Hashtable<String, String>());
	static Map<String, String> map1 = new Hashtable<String, String>();
	// static Map<String, String> map2 = new Hashtable<String, String>();
	static Map<String, String> map2 = Collections.synchronizedMap(new HashMap<String, String>());
	public static void main(String[] args) throws Exception {

		Thread1 thread1 = new Thread1();
		Thread3 thread3 = new Thread3();

		thread1.start();
		thread3.start();

		thread1.join();
		thread3.join();

	}

	static class Thread1 extends Thread {

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				map1.put("abc" + i, "abc" + i);
			}
			System.out.println("thread1 ok"
					+ (System.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				map1.get("abc" + i);
			}
			System.out.println("thread2 ok"
					+ (System.currentTimeMillis() - start));
		}

	}

	static class Thread3 extends Thread {

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				map2.put("abc" + i, "abc" + i);
			}

			System.out.println("thread3 ok"
					+ (System.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			for (int i = 0; i < 1000000; i++) {
				map2.get("abc" + i);
			}
			System.out.println("thread4 ok"
					+ (System.currentTimeMillis() - start));
		}

	}

}
