package com.taobao.pamirs.cache;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class Test {

	public static void main(String[] args) {
		ArrayList<String> arrayList = new ArrayList<String>();
		LinkedList<String> linkedList = new LinkedList<String>();
		Vector<String> vector = new Vector<String>();

		int num = 100000;
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			arrayList.add("abc" + i);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			String s = arrayList.get(i);
		}
		end = System.currentTimeMillis();
		
		System.out.println(end - start);
		
		
		
		
		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			vector.add("abc" + i);
		}
		end = System.currentTimeMillis();
		System.out.println(end - start);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			String s = vector.get(i);
		}
		end = System.currentTimeMillis();
		
		System.out.println(end - start);
		
		
		
		
		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			linkedList.add("abc" + i);
		}
		end = System.currentTimeMillis();
		System.out.println(end - start);
		
		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			String s = linkedList.get(i);
		}
		end = System.currentTimeMillis();
		
		System.out.println(end - start);
		
		
	}

}
