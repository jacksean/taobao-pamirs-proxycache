package com.taobao.pamirs.cache.log.xray;

import com.taobao.pamirs.cache.framework.listener.CacheOprateInfo;
import com.taobao.pamirs.cache.framework.listener.CacheOprateListener;
import com.taobao.pamirs.cache.framework.listener.CacheOprator;
import com.taobao.pamirs.cache.log.asynlog.AsynWriter;

/**
 * 打印给xray统计
 * 
 * @author xiaocheng 2012-11-13
 */
public class XrayLogListener implements CacheOprateListener {

	private AsynWriter<String> writer = new AsynWriter<String>();

	private String beanName;
	private String methodName;

	private static final String SEPARATOR = ",";

	public XrayLogListener(String beanName, String methodName) {
		this.beanName = beanName;
		this.methodName = methodName;
	}

	@Override
	public void oprate(CacheOprator oprator, CacheOprateInfo cacheInfo) {
		log(oprator, cacheInfo.isHitting(), cacheInfo.getMethodTime());
	}

	/**
	 * 打印日志
	 * 
	 * @param type
	 * @param isHit
	 * @param useTime
	 */
	private void log(CacheOprator type, boolean isHit, long useTime) {
		StringBuilder sb = new StringBuilder();
		sb.append(SEPARATOR).append(beanName);
		sb.append(SEPARATOR).append(methodName);
		sb.append(SEPARATOR).append(type.name());
		sb.append(SEPARATOR).append(isHit);
		sb.append(SEPARATOR).append(useTime);

		writer.write(sb.toString());
	}

}