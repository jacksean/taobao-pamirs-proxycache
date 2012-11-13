package com.taobao.pamirs.cache.log.asynlog;

import org.apache.commons.logging.Log;

/**
 * �첽��־������Ϣ
 * 
 * @author xiaocheng 2012-11-9
 */
public class LogConfig {

	private static LogConfig config = new LogConfig();

	/**
	 * �첽��־�̳߳ش�С
	 */
	private int asynWriterThreadSize = 1;

	/**
	 * ��־�б������¼��
	 */
	private int recordsMaxSize = 5000;

	/**
	 * ��־ˢ��ʱ����(��λ:��)
	 */
	private int flushInterval = 2;

	/**
	 * ��־LOG
	 */
	private Log log;

	private LogConfig() {
	}

	public static LogConfig getInstance() {
		return config;
	}

	public int getAsynWriterThreadSize() {
		return asynWriterThreadSize;
	}

	public void setAsynWriterThreadSize(int asynWriterThreadSize) {
		this.asynWriterThreadSize = asynWriterThreadSize;
	}

	public int getRecordsMaxSize() {
		return recordsMaxSize;
	}

	public void setRecordsMaxSize(int recordsMaxSize) {
		this.recordsMaxSize = recordsMaxSize;
	}

	public int getFlushInterval() {
		return flushInterval;
	}

	public void setFlushInterval(int flushInterval) {
		this.flushInterval = flushInterval;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

}
