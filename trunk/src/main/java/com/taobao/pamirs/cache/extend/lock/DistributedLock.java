package com.taobao.pamirs.cache.extend.lock;

/**
 * 分布式锁（悲观锁，当前不支持‘重入’）
 * 
 * <pre>
 * boolean lockSuccess = distributedLock.lock(1L, &quot;abc&quot;);
 * if (lockSuccess) {
 * 	try {
 * 		// do something
 * 	} finally {
 * 		distributedLock.unlock(1L, &quot;abc&quot;);
 * 	}
 * }
 * </pre>
 * 
 * @author xiaocheng Aug 18, 2015
 */
public interface DistributedLock {

	public static final int DEFAULT_EXPIRE_SECONDS = 10;// 默认锁过期时间

	/**
	 * 尝试获取分布式锁（采用默认锁过期时间）
	 * 
	 * @param objType
	 * @param objId
	 */
	boolean lock(long objType, String objId);

	/**
	 * 尝试获取分布式锁
	 * 
	 * @param objType
	 * @param objId
	 * @param expireSeconds
	 *            指定锁过期时间
	 */
	boolean lock(long objType, String objId, int expireSeconds);

	/**
	 * 释放锁
	 * 
	 * @param objType
	 * @param objId
	 * @return 当释放失败包装异常，返回false，可以重试或等待锁超时
	 */
	boolean unlock(long objType, String objId);

}
