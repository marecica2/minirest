package org.bmsource.minirest.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bmsource.minirest.internal.container.Container;
import org.bmsource.minirest.internal.container.WeldContainerImpl;

public class MinirestBootstrap implements ServletContextListener {
	public static String MINIREST_CONTAINER_ATTRIBUTE = "minirest.container";

	private Container container;

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		container = new WeldContainerImpl();
		container.start();
		servletContext.setAttribute(MINIREST_CONTAINER_ATTRIBUTE, container);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		container.stop();
	}
}
