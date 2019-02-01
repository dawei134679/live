package com.tinypig.admin.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 在无法用注解自动装载的时候用此类来进行bean的获取
 * 
 */
public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext app;

	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		app = ctx;
	}

	public static ApplicationContext getApp() {
		return app;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
		return (T) app.getBean(beanName);
	}
}