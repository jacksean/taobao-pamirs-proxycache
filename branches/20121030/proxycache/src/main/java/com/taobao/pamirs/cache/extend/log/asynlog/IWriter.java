package com.taobao.pamirs.cache.extend.log.asynlog;

/**
 * �첽Log�ӿ�
 * 
 * @author xiaocheng 2012-11-9
 */
public interface IWriter<T> {

	/**
	 * ����д
	 * 
	 * @param content
	 */
	public void write(T content);

}
