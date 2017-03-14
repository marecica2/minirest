package org.bmsource.minirest.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.bmsource.minirest.internal.container.Container;
import org.bmsource.minirest.internal.container.WeldContainerImpl;

public class MinirestBootstrap {
	public static String MINIREST_CONTAINER_ATTRIBUTE = "minirest.container";

	private Container container;

	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();

		// ListenerBootstrap config = new
		// ListenerBootstrap(event.getServletContext());
		// deployment = config.createDeployment();
		// deployment.start();

		container = new WeldContainerImpl();
		container.start();
		servletContext.setAttribute(MINIREST_CONTAINER_ATTRIBUTE, container);
	}

	public void contextDestroyed(ServletContextEvent event) {
		container.stop();
	}
}
