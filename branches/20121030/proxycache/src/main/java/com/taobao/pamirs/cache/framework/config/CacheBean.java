package com.taobao.pamirs.cache.framework.config;

/**
 * ����bean����
 * 
 * @author xiaocheng 2012-11-2
 */
public class CacheBean extends MethodConfig {

	//
	private static final long serialVersionUID = 4973185401294689002L;

	/**
	 * ʧЧʱ�䣬��λ���롣<br>
	 * ���������ʱ�䣬Ҳ�����Ǿ���ʱ��(���ڵ�ǰʱ����Ǿ���ʱ�����)��������0���ǲ�����
	 */
	private Integer expiredTime;

	public Integer getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Integer expiredTime) {
		this.expiredTime = expiredTime;
	}

}
