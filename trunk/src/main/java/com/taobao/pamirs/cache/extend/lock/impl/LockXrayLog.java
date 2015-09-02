package com.taobao.pamirs.cache.extend.lock.impl;

import org.apache.commons.logging.LogFactory;

import com.taobao.pamirs.cache.util.asynlog.AsynWriter;
import com.taobao.tair.Result;
import com.taobao.tair.ResultCode;

public class LockXrayLog {

	private static AsynWriter<String> log = new AsynWriter<String>(
			LogFactory.getLog(LockXrayLog.class));

	public static final String LOCK = "TRY_LOCK";
	public static final String UNLOCK = "UN_LOCK";

	private static final String SEPARATOR = ",";

	public static void write(String method, long useTime, boolean success,
			long objType, String objId, int expireSeconds, Result<?> r,
			ResultCode rc) {
		log.write(getXrayLog(method, useTime, success, objType, objId,
				expireSeconds, r, rc));
	}

	private static String getXrayLog(String method, long useTime,
			boolean success, long objType, String objId, int expireSeconds,
			Result<?> r, ResultCode rc) {
		StringBuilder sb = new StringBuilder();
		sb.append(method);
		sb.append(SEPARATOR).append(success);
		sb.append(SEPARATOR).append(useTime);

		sb.append(SEPARATOR).append(objType);
		sb.append(SEPARATOR).append(objId);
		sb.append(SEPARATOR).append(expireSeconds);

		sb.append(SEPARATOR).append(
				r == null ? null : (r.getRc() == null ? null : r.getRc()
						.getCode()));
		sb.append(SEPARATOR).append(r == null ? null : r.getValue());
		sb.append(SEPARATOR).append(rc == null ? null : rc.getCode());

		System.out.println(sb.toString());
		return sb.toString();
	}

}
