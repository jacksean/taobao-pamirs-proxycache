package com.taobao.pamirs.cache.util;

import com.taobao.pamirs.cache.util.convert.OldVersionCacheConfigConvert;

/**
 * TODO 类说明
 * 
 * @author qiudao
 * @version 1.0
 * @since 2014年10月31日
 */
public class OldVersionCacheConfigConvertTest {

	public static void main(String[] args) throws Exception {
		String path = "D:/ws/ws_tmall_dev/proxycache/src/test/resources/convert/biz-cache.xml";
		OldVersionCacheConfigConvert.convertToNewFile(path);
	}
}
