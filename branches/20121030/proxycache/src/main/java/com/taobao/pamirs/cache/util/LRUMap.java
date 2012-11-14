package com.taobao.pamirs.cache.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class LRUMap<K, V> extends LinkedHashMap<K, V> {

	//
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
		a();
		System.out.println("================");
		b();
		System.out.println("================");
		c();
		System.out.println("================");
	}

	public static void a() {
		// 遍历：后放先出
		Map<String, String> map = new HashMap<String, String>();
		map.put("a3", "aa");
		map.put("b1", "cc");
		map.put("a2", "bb");
		map.put("b2", "b2");
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			System.out.println(name);
		}
	}

	public static void b() {
		// 遍历：怎么样放怎么样出
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("a3", "aa");
		map.put("b1", "cc");
		map.put("a2", "bb");
		map.put("b2", "b2");
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			System.out.println(name);
		}
	}

	public static void c() {
		// 遍历：不管怎么样放，出的时候都是按key的排序（升序）出。
		Map<String, String> map = new TreeMap<String, String>();
		map.put("a3", "aa");
		map.put("b1", "cc");
		map.put("a2", "bb");
		map.put("b2", "b2");
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			System.out.println(name);
		}
	}

}
