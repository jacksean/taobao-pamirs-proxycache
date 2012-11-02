package com.taobao.pamirs.cache2.framework.config;

import java.io.Serializable;
import java.util.List;

/**
 * ª˘±æbean≈‰÷√
 * 
 * @author xiaocheng 2012-11-2
 */
public class MethodConfig implements Serializable {

	//
	private static final long serialVersionUID = 1L;

	private String beanName;
	private String methodName;
	private List<Class<?>> parameterTypes;

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<Class<?>> getParameterTypes() {
		return parameterTypes;
	}

	public void setParameterTypes(List<Class<?>> parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	
	public boolean isMe(String name, String method, List<Class<?>> types) {
		if (!this.beanName.equals(name))
			return false;
		
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
				if (!parameterTypes.get(i).getSimpleName().equals(types.get(i).getSimpleName()))
					return false;
			}
			
		}
		
		return true;
	}
	
}
