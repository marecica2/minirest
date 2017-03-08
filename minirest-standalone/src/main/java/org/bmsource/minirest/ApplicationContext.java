package org.bmsource.minirest;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Response;

import org.bmsource.minirest.internal.RequestHandler;
import org.bmsource.minirest.internal.container.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContext implements Comparable<ApplicationContext> {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String contextPath;
	private RequestHandler handler;
	private Container container;

	public ApplicationContext(String contextPath, RequestHandler handler, Container container) {
		contextPath = normalizeContextPath(contextPath);
		this.contextPath = contextPath;
		this.handler = handler;
		this.container = container;
	}

	public boolean isHandlingRequest(MiniRequest request) {
		return (request.getAbsoluteLocation().startsWith(this.contextPath));
	}

	Response handleRequest(MiniRequest request)
			throws NotAllowedException, NotSupportedException, NotAcceptableException {
		logger.debug("Context {} handling the request {}", contextPath, request.getRelativePath());
		return handler.handle(request, container);
	}

	public String getContextPath() {
		return contextPath;
	}

	public RequestHandler getHandler() {
		return handler;
	}

	@Override
	public int compareTo(ApplicationContext c) {
		return -1 * (this.contextPath.compareTo(c.getContextPath()));
	}

	private String normalizeContextPath(String contextPath) {
		if (!contextPath.startsWith("/")) {
			contextPath = "/" + contextPath;
		}
		if (contextPath.endsWith("/")) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
		}
		return contextPath;
	}
}
