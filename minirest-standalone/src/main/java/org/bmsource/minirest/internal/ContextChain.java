package org.bmsource.minirest.internal;

import java.util.SortedSet;
import java.util.TreeSet;

import org.bmsource.minirest.ApplicationContext;
import org.bmsource.minirest.MiniRequest;
import org.bmsource.minirest.internal.container.Container;

public class ContextChain {
	private SortedSet<ApplicationContext> contextSet = new TreeSet<>();

	/**
	 * Registers a new context
	 *
	 * @param path
	 *            specific path where the context listens
	 * @param handler
	 * @return currently created context
	 */
	public ApplicationContext createContext(String path, RequestHandler handler, Container container) {
		ApplicationContext httpContext = new ApplicationContext(path, handler, container);
		contextSet.add(httpContext);
		return httpContext;
	}

	/**
	 * Returns a context bound to the specific request url
	 *
	 * @param requestURI
	 * @return Context or null
	 */
	public ApplicationContext getHandlerContext(final MiniRequest request) {
		for (ApplicationContext context : contextSet) {
			if (context.isHandlingRequest(request)) {
				return context;
			}
		}
		return null;
	}
}
