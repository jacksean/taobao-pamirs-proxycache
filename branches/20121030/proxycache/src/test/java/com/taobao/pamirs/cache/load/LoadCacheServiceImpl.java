package com.taobao.pamirs.cache.load;

/**
 * ���ػ������
 * 
 * @author poxiao.gj
 * 
 */
public class LoadCacheServiceImpl implements LoadCacheSerivce {

	public String getCacheName(String name) {
		return "�������ƣ�" + name;
	}

	public String getCacheCount(Long count) {
		return "���������" + count;
	}

	@Override
	public void cleanCacheByName(String name) {
		
	}

	@Override
	public void cleanCacheByCount(Long count) {
		
	}

}
