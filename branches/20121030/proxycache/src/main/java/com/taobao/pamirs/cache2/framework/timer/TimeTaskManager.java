package com.taobao.pamirs.cache2.framework.timer;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.pamirs.cache.framework.CacheAdapter;

public class TimeTaskManager {
	private Timer timer;
	private Map<String,CacheManagerTimerTask> taskMap = new ConcurrentHashMap<String,CacheManagerTimerTask>();
	
	public TimeTaskManager(){
		timer = new Timer("CacheManagerTimeTaskManager");
	}
	public void createCleanCacheTask(CacheAdapter<String,Object> Cache,String aCronTabExpress) throws Exception{
		CronExpression cexp = new CronExpression(aCronTabExpress);
		Date current = new Date(System.currentTimeMillis());
		Date nextTime = cexp.getNextValidTimeAfter(current);
		CacheManagerTimerTask task = new CacheManagerTimerTask(this,Cache,aCronTabExpress);
		taskMap.put(Cache.getCacheName(), task);
		this.timer.schedule(task, nextTime);
	}
	public void removeCleanCacheTask(String cacheName){
		CacheManagerTimerTask task = this.taskMap.remove(cacheName);
		task.cancel();
	}
}

class  CacheManagerTimerTask extends TimerTask{
	private static transient Log log = LogFactory.getLog(CacheManagerTimerTask.class);
	CacheAdapter<String,Object> Cache;
	String cronTabExpress;
	TimeTaskManager manager;
	public CacheManagerTimerTask(TimeTaskManager aManager,CacheAdapter<String,Object> aCache,String aCronTabExpress){
		this.manager = aManager;
		this.Cache = aCache;
		this.cronTabExpress = aCronTabExpress;
	}
	public void run() {
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		this.manager.removeCleanCacheTask(this.Cache.getCacheName());
		try{
			this.Cache.clear();
		}finally{
			try{
				this.manager.createCleanCacheTask(this.Cache,this.cronTabExpress);
			}catch(Exception e){
				log.fatal("���ش��󣬶�ʱ������ʧ�ܣ�" + e.getMessage(),e);
			}

		}
	}
}

class CleanCacheTask implements Runnable{
	CacheAdapter<String,Object> item;
	CleanCacheTask(CacheAdapter<String,Object> aItem){
		this.item =aItem;
	}
	public void run() {
		 this.item.clear();	
	}	
}