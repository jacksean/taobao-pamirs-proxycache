package com.taobao.pamirs.cache.log.asynlog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ��־д����
 * 
 * @author xiaocheng 2012-11-9
 */
public class WriterTask<T> implements Runnable {

	private static final Log log = LogFactory.getLog(AsynWriter.class);

	private static final String LINE_SEPARATOR = "\r\n";

	/**
	 * ��־����
	 */
	private BlockingQueue<T> logQueue;

	/**
	 * ���ö���Ӧ��Ψһһ��
	 */
	private LogConfig config;

	/**
	 * ����
	 */
	private volatile boolean activeFlag = true;

	private List<T> records = new ArrayList<T>();

	private long timestamp;

	@Override
	public void run() {

		try {
			while (activeFlag) {

				// ��¼��
				if (records.size() > config.getRecordsMaxSize()) {
					flush();
				}

				// ��ʱ
				if (records.size() > 0
						&& System.currentTimeMillis() > (timestamp + config
								.getFlushInterval() * 100L)) {
					flush();
				}

				T r = logQueue.poll(100, TimeUnit.MILLISECONDS);
				if (r != null)
					records.add(r);
			}
		} catch (Exception e) {
			log.error("�����ء���־����ʧ��!", e);
		}

	}

	private void flush() {
		Log logWriter = config.getLog();
		if (logWriter == null)
			logWriter = log;

		for (T r : records) {
			if (logWriter.isFatalEnabled()) {
//				logWriter.fatal(r);
//				logWriter.fatal(LINE_SEPARATOR);
				
			}
		}

		records.clear();
		timestamp = System.currentTimeMillis();
	}

	public BlockingQueue<T> getLogQueue() {
		return logQueue;
	}

	public void setLogQueue(BlockingQueue<T> logQueue) {
		this.logQueue = logQueue;
	}

	public boolean isActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

}
