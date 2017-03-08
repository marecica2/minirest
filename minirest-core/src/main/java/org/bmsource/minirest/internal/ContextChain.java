package org.bmsource.minirest.internal;

import java.util.SortedSet;
import java.util.TreeSet;

import org.bmsource.minirest.HttpContext;
import org.bmsource.minirest.internal.cdi.Container;

public class ContextChain {
	private SortedSet<HttpContext> contextSet = new TreeSet<>();

	/**
	 * Registers a new context
	 *
	 * @param path
	 *            specific path where the context listens
	 * @param handler
	 * @return currently created context
	 */
	public HttpContext createContext(String path, RequestHandler handler, Container container) {
		HttpContext httpContext = new HttpContext(path, handler, container);
		contextSet.add(httpContext);
		return httpContext;
	}

	/**
	 * Returns a context bound to the specific request url
	 *
	 * @param requestURI
	 * @return Context or null
	 */
	public HttpContext getHandlerContext(final ContainerRequest request) {
		for (HttpContext context : contextSet) {
			if (context.isHandlingRequest(request)) {
				return context;
			}
		}
		return null;
	}
}
