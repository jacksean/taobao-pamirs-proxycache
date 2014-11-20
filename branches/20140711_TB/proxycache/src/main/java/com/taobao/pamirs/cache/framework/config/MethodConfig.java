package com.taobao.pamirs.cache.framework.config;

import java.io.Serializable;
import java.util.List;

import com.taobao.pamirs.cache.load.verify.Verfication;
import com.taobao.pamirs.cache.store.RemoveMode;

/**
 * ����bean����
 * 
 * @author xiaocheng 2012-11-2
 */
public class MethodConfig implements Serializable {

	//
	private static final long serialVersionUID = 1L;

	@Verfication(name = "��������", notEmpty = true)
	private String methodName;
	/**
	 * ��������
	 */
	@Verfication(name = "��������", notEmptyList = true)
	private List<Class<?>> parameterTypes;

	/**
	 * ʧЧʱ�䣬��λ���롣<br>
	 * ���������ʱ�䣬Ҳ�����Ǿ���ʱ��(���ڵ�ǰʱ����Ǿ���ʱ�����)��������0���ǲ����� <br>
	 * ����ѡ�
	 */
	private Integer expiredTime;
	
	/**
	 * �÷����Ƿ������棬ȱʡ����
	 */
	private boolean useCache=true;
	
	/**
	 * �÷���ʧЧ����ʹ��hidden��ʽ��Ĭ��invaild��tair��
	 */
	private String removeMode=RemoveMode.INVAILD.getName();
	

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * null: ����û��set,װ������ʱ��Ҫ���¸�ֵ <br>
	 * ��: �����޲η���
	 * 
	 * @return
	 */
	public List<Class<?>> getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(List<Class<?>> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}

	public Integer getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(Integer expiredTime) {
		this.expiredTime = expiredTime;
	}

	public boolean isMe(String method, List<Class<?>> types) {
		if (!this.methodName.equals(method))
			return false;

		if (this.parameterTypes == null && types != null)
			return false;

		if (this.parameterTypes != null && types == null)
			return false;

		if (this.parameterTypes != null) {
			if (this.parameterTypes.size() != types.size())
				return false;

			for (int i = 0; i < parameterTypes.size(); i++) {
				if (!parameterTypes.get(i).getSimpleName()
						.equals(types.get(i).getSimpleName()))
					return false;
			}

		}

		return true;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public String getRemoveMode() {
		return removeMode;
	}

	public void setRemoveMode(String removeMode) {
		this.removeMode = removeMode;
	}



}