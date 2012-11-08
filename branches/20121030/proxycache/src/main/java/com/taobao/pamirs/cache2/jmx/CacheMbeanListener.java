package com.taobao.pamirs.cache2.jmx;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

import com.taobao.pamirs.cache2.framework.listener.CacheInfo;
import com.taobao.pamirs.cache2.framework.listener.CacheOprateListener;
import com.taobao.pamirs.cache2.framework.listener.CacheOprator;

import static com.taobao.pamirs.cache2.framework.listener.CacheOprator.*;

/**
 * Cache Mbean信息处理计数类
 * 
 * @author xiaocheng 2012-11-7
 */
public class CacheMbeanListener implements CacheOprateListener {

	/**
	 * 统计数据的时间窗口，单位：毫秒
	 */
	private static final long timeWindows = 120 * 1000L;// 默认2分钟

	/**
	 * 最后一次清理的时间
	 */
	private long lastClearTime = System.currentTimeMillis();

	/**
	 * GET操作命中次数
	 */
	private AtomicLong readSuccessCount = new AtomicLong(0);
	/**
	 * GET操作未命中次数
	 */
	private AtomicLong readFailCount = new AtomicLong(0);
	/**
	 * GET操作总用时
	 */
	private AtomicLong readTotalTime = new AtomicLong(0);

	/**
	 * PUT操作次数
	 */
	private AtomicLong writeCount = new AtomicLong(0);
	/**
	 * PUT操作总用时
	 */
	private AtomicLong writeTotalTime = new AtomicLong(0);

	/**
	 * REMOVE清理总次数
	 */
	private AtomicLong removeCount = new AtomicLong(0);

	@Override
	public void oprate(CacheOprator oprator, CacheInfo cacheInfo) {
		if (oprator == null || cacheInfo == null)
			return;

		// 刷新统计
		if (System.currentTimeMillis() > (lastClearTime + timeWindows)) {
			clear();
		}

		if (oprator == GET) {
			if (cacheInfo.isHitting()) {
				readSuccessCount.incrementAndGet();
			} else {
				readFailCount.incrementAndGet();
			}

			readTotalTime.addAndGet(cacheInfo.getMethodTime());
		} else if (oprator == PUT || oprator == PUT_EXPIRE) {
			writeCount.incrementAndGet();
			writeTotalTime.addAndGet(cacheInfo.getMethodTime());
		} else if (oprator == REMOVE) {
			removeCount.incrementAndGet();
		}

	}

	/**
	 * 缓存命中率
	 * 
	 * @return
	 */
	public String getReadHitRate() {
		long successCount = readSuccessCount.get();
		long failCount = readFailCount.get();
		long allCount = successCount + failCount;

		if (allCount == 0)
			return "%";

		BigDecimal hitRated = new BigDecimal(successCount * 100 / allCount);
		return hitRated.toString() + "%";
	}

	public long getReadAvgTime() {
		long successCount = readSuccessCount.get();
		long failCount = readFailCount.get();
		long allCount = successCount + failCount;
		long totalTime = readTotalTime.get();

		if (allCount == 0)
			return 0L;

		return totalTime / allCount;
	}

	public long getWriteAvgTime() {

		long count = writeCount.get();
		long totalTime = writeTotalTime.get();

		if (count == 0)
			return 0L;

		return totalTime / count;
	}

	/**
	 * 清空统计数据.
	 */
	private void clear() {
		readSuccessCount.set(0);
		readFailCount.set(0);
		readTotalTime.set(0);
		writeCount.set(0);
		writeCount.set(0);
		removeCount.set(0);

		lastClearTime = System.currentTimeMillis();
	}

	public AtomicLong getReadSuccessCount() {
		return readSuccessCount;
	}

	public void setReadSuccessCount(AtomicLong readSuccessCount) {
		this.readSuccessCount = readSuccessCount;
	}

	public AtomicLong getReadFailCount() {
		return readFailCount;
	}

	public void setReadFailCount(AtomicLong readFailCount) {
		this.readFailCount = readFailCount;
	}

	public AtomicLong getReadTotalTime() {
		return readTotalTime;
	}

	public void setReadTotalTime(AtomicLong readTotalTime) {
		this.readTotalTime = readTotalTime;
	}

	public AtomicLong getWriteCount() {
		return writeCount;
	}

	public void setWriteCount(AtomicLong writeCount) {
		this.writeCount = writeCount;
	}

	public AtomicLong getWriteTotalTime() {
		return writeTotalTime;
	}

	public void setWriteTotalTime(AtomicLong writeTotalTime) {
		this.writeTotalTime = writeTotalTime;
	}

	public AtomicLong getRemoveCount() {
		return removeCount;
	}

	public void setRemoveCount(AtomicLong removeCount) {
		this.removeCount = removeCount;
	}

}
