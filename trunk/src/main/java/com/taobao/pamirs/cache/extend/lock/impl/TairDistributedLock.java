package com.taobao.pamirs.cache.extend.lock.impl;

import static com.taobao.pamirs.cache.extend.lock.impl.LockXrayLog.LOCK;
import static com.taobao.pamirs.cache.extend.lock.impl.LockXrayLog.UNLOCK;
import static com.taobao.pamirs.cache.extend.lock.impl.LockXrayLog.write;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taobao.pamirs.cache.extend.lock.DistributedLock;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;
import com.taobao.tair.TairManager;

/**
 * Tair实现的分布式锁
 * 
 * @author xiaocheng Aug 17, 2015
 */
public class TairDistributedLock implements DistributedLock {

	private static final Logger log = LoggerFactory
			.getLogger(TairDistributedLock.class);

	private TairManager tairManager;
	private Integer namespace;
	private String region;

	private static final int CUR_VALUE = 1;

	@Override
	public boolean lock(long objType, String objId) {
		return this.lock(objType, objId, DEFAULT_EXPIRE_SECONDS);
	}

	@Override
	public boolean lock(long objType, String objId, int es) {
		long start = System.currentTimeMillis();
		String key = combineKey(objType, objId);
		boolean success = false;
		Result<Integer> incr = null;

		try {
			incr = tairManager.incr(namespace, key, CUR_VALUE, 0, es);

			// 第一次超时等异常，自动重试一次
			if (isTimeout(incr.getRc()))
				incr = tairManager.incr(namespace, key, CUR_VALUE, 0, es);

			// 获取锁
			if (!isTimeout(incr.getRc()) && incr.getValue() != null
					&& incr.getValue().intValue() == CUR_VALUE)
				success = true;
		} catch (Throwable e) {
			log.error("Get Lock Fail!", e);
		}

		long end = System.currentTimeMillis();
		write(LOCK, end - start, success, objType, objId, es, incr, null);

		return success;
	}

	@Override
	public boolean unlock(long objType, String objId) {
		long start = System.currentTimeMillis();
		boolean success = false;
		ResultCode rc = null;

		try {
			rc = tairManager.delete(namespace, combineKey(objType, objId));

			// 第一次超时等异常，自动重试一次
			if (isTimeout(rc))
				rc = tairManager.delete(namespace, combineKey(objType, objId));

			success = rc.isSuccess();
		} catch (Throwable e) {
			log.error("Release Lock Fail!", e);
		}

		long end = System.currentTimeMillis();
		write(UNLOCK, end - start, success, objType, objId, 0, null, rc);

		return success;
	}

	/**
	 * 组装生成key
	 */
	private String combineKey(long objType, String objId) {
		StringBuilder sb = new StringBuilder();
		if (isNotEmpty(region))
			sb.append(region).append("@");

		sb.append(objType).append("$").append(objId);
		return sb.toString();
	}

	/**
	 * Tair timeout judge
	 */
	private boolean isTimeout(ResultCode rc) {
		return ResultCode.CONNERROR.equals(rc) || ResultCode.TIMEOUT.equals(rc)
				|| ResultCode.UNKNOW.equals(rc);
	}

	public void setTairManager(TairManager tairManager) {
		this.tairManager = tairManager;
	}

	public void setNamespace(Integer namespace) {
		this.namespace = namespace;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
