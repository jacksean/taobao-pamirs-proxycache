package com.taobao.pamirs.cache.store.threadcache;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.core.PriorityOrdered;

import com.taobao.fuwu.util.jmx.annotation.JmxClass;
import com.taobao.fuwu.util.jmx.annotation.JmxMethod;

/**
 * �̻߳���
 * 
 * @author xiaocheng 2012-9-3
 */
@JmxClass
public class ThreadCacheHandle extends AbstractAutoProxyCreator implements
		BeanFactoryAware, PriorityOrdered {

	private static final Log log = LogFactory.getLog(ThreadCacheHandle.class);

	/**  */
	private static final long serialVersionUID = 1L;

	/** �����أ�Ĭ��beanע�ἴ���� */
	private boolean openThreadCache = true;

	/** ������־���� */
	private boolean printHitLog = false;

	/** ��ӡ��ϸ��־�����з������ؽ������� */
	private boolean printLogDetail = false;

	/**
	 * ������--ע�����һ��ѡ��ʽ
	 */
	private Map<String, List<String>> beansMap;

	public ThreadCacheHandle() {
		super.setOrder(LOWEST_PRECEDENCE);
	}

	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(
			@SuppressWarnings("rawtypes") Class beanClass, String beanName,
			TargetSource customTargetSource) throws BeansException {
		if (this.beansMap != null && this.beansMap.keySet().contains(beanName)) {

			if (log.isWarnEnabled()) {
				log.warn("�����߳�cache����" + beanClass + ":" + beanName);
			}

			if (targetBeanIsFinal(beanClass)) {// must implements a interface
				this.setProxyTargetClass(false);// JDK
			} else {
				this.setProxyTargetClass(true);// CGLIB
			}

			return new ThreadMethodAdvisor[] { new ThreadMethodAdvisor(
					beanClass, beanName, this) };
		}
		return DO_NOT_PROXY;
	}

	private boolean targetBeanIsFinal(Class<?> clazz) {
		String inMods = Modifier.toString(clazz.getModifiers());
		if (inMods.contains("final")) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isOpenThreadCache() {
		return openThreadCache;
	}

	@JmxMethod
	public void setOpenThreadCache(boolean openThreadCache) {
		this.openThreadCache = openThreadCache;
	}

	public boolean isPrintHitLog() {
		return printHitLog;
	}

	@JmxMethod
	public void setPrintHitLog(boolean printHitLog) {
		this.printHitLog = printHitLog;
	}

	public boolean isPrintLogDetail() {
		return printLogDetail;
	}

	@JmxMethod
	public void setPrintLogDetail(boolean printLogDetail) {
		this.printLogDetail = printLogDetail;
	}

	public Map<String, List<String>> getBeansMap() {
		return beansMap;
	}

	public void setBeansMap(Map<String, String> beansMapString) {
		if (beansMapString == null)
			this.beansMap = null;

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		Iterator<String> it = beansMapString.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			map.put(key, Arrays.asList(beansMapString.get(key).split(",")));
		}

		this.beansMap = map;
	}

}

class ThreadMethodAdvisor implements Advisor {

	ThreadMethodRoundAdvice advice;

	public ThreadMethodAdvisor(Class<?> aBeanClass, String beanName,
			ThreadCacheHandle handle) {
		advice = new ThreadMethodRoundAdvice(aBeanClass, beanName, handle);
	}

	@Override
	public Advice getAdvice() {
		return advice;
	}

	@Override
	public boolean isPerInstance() {
		return false;
	}

}

class ThreadMethodRoundAdvice implements Advice, MethodInterceptor {

	private static final Log log = LogFactory.getLog(ThreadCacheHandle.class);

	@SuppressWarnings("unused")
	private Class<?> beanClass;
	private ThreadCacheHandle handle;
	private String beanName;

	public ThreadMethodRoundAdvice(Class<?> aBeanClass, String beanName,
			ThreadCacheHandle handle) {
		this.beanClass = aBeanClass;
		this.beanName = beanName;
		this.handle = handle;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		String methodName = invocation.getMethod().getName();
		List<String> mothods = handle.getBeansMap().get(beanName);

		if (!handle.isOpenThreadCache() || mothods == null
				|| !mothods.contains(methodName)) {
			// do nothing
			return invocation.proceed();
		}
		
		String key = null;
		try {
			key = getCacheKey(methodName, invocation);
		} catch (IllegalArgumentException e) {
			log.warn("�в�֧�ֵĲ������ͣ��޷����ɻ���key������������棡" + beanName + ":" + methodName);
			return invocation.proceed();
		}

		Object value = ThreadContext.get(key);
		// 1. cache ��ֵ��ֱ�ӷ���
		if (value != null) {
			doPrintLog(key, value);
			return value;
		}

		// 2. û�л��棬��ԭ���߼�����ɺ�ע��cache
		value = invocation.proceed();
		ThreadContext.put(key, value);

		return value;
	}

	/**
	 * ���ɻ���key����
	 * 
	 * @param methodName
	 * @param invocation
	 * @return
	 */
	private String getCacheKey(String methodName, MethodInvocation invocation) {
		StringBuilder args = new StringBuilder();

		Object[] paramObjects = invocation.getArguments();
		Class<?>[] paramTypes = invocation.getMethod().getParameterTypes();

		if (paramObjects != null) {
			for (int i = 0; i < paramObjects.length; i++) {
				if (!isPrimitive(paramTypes[i]))
					throw new IllegalArgumentException();

				if (args.toString().length() != 0)
					args.append(":");

				args.append(paramObjects[i]);
			}
		}

		return beanName + "$" + methodName + "$" + args.toString();
	}
	
	/**
	 * Ĭ��֧�ֵ�ԭʼ����
	 * 
	 * @param arg
	 * @return
	 */
	private boolean isPrimitive(Class<?> type) {
		if (type.isPrimitive() || Byte.class.isAssignableFrom(type)
				|| Short.class.isAssignableFrom(type)
				|| Integer.class.isAssignableFrom(type)
				|| Long.class.isAssignableFrom(type)
				|| Float.class.isAssignableFrom(type)
				|| Double.class.isAssignableFrom(type)
				|| Character.class.isAssignableFrom(type)
				|| String.class.isAssignableFrom(type)
				|| Date.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type))
			return true;
		
		return false;
	}
	
	private void doPrintLog(String key, Object value) {
		if (handle.isPrintHitLog()) {
			StringBuilder logInfo = new StringBuilder("�̻߳�������!");
			logInfo.append("key=").append(key);

			if (handle.isPrintLogDetail()) {
				logInfo.append("��value=").append(value);
			}

			log.warn(logInfo.toString());
		}
	}
}
