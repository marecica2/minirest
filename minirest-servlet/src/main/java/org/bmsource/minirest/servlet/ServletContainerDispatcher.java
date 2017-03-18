package org.bmsource.minirest.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.bmsource.minirest.MiniRequest;
import org.bmsource.minirest.internal.JaxRsRequestHandler;
import org.bmsource.minirest.internal.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletContainerDispatcher {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static String JAVAX_WS_RS_APPLICATION_PARAMETER = "javax.ws.rs.Application";

	private ServletConfig servletConfig;
	private JaxRsRequestHandler<?> handler;
	private Container container;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ServletContainerDispatcher(ServletConfig servletConfig) {
		try {

			servletConfig.getServletContext().getResourcePaths("/WEB-INF/lib");

			final String applicationName = servletConfig.getInitParameter(JAVAX_WS_RS_APPLICATION_PARAMETER);
			final Class<? extends Application> applicationClass = (Class<? extends Application>) servletConfig
					.getServletContext().getClassLoader().loadClass(applicationName);

			this.servletConfig = servletConfig;
			this.handler = new JaxRsRequestHandler(applicationClass);
			this.container = (Container) servletConfig.getServletContext()
					.getAttribute(MinirestBootstrap.MINIREST_CONTAINER_ATTRIBUTE);

		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public void destroy() {
	}

	public void service(String httpMethod, HttpServletRequest httpRequest, HttpServletResponse response) {
		MiniRequest request = MiniRequestBuilder.build(httpRequest);
		Response jaxrsResponse = handler.handle(request, container);
		writeResponse(httpRequest, response, jaxrsResponse);
	}

	protected void writeResponse(HttpServletRequest request, HttpServletResponse response, Response jaxrsResponse) {
		try {
			ServerResponseWriter.writeResponse(jaxrsResponse, request, response);
		} catch (Exception e) {
			logger.error("writeResponse() failed mapping exception", e);
		}
	}

}
