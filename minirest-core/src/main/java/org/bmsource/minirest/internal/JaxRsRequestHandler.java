package org.bmsource.minirest.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.RuntimeDelegate;

import org.bmsource.minirest.MiniRequest;
import org.bmsource.minirest.internal.container.Container;
import org.bmsource.minirest.internal.jaxrs.InvokableInjector;
import org.bmsource.minirest.internal.jaxrs.RuntimeDelegateImpl;
import org.bmsource.minirest.internal.jaxrs.routing.JaxRsRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaxRsRequestHandler<A extends Application> implements RequestHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private A application;
	private JaxRsRouter<A> router;

	static {
		RuntimeDelegateImpl delegate = new RuntimeDelegateImpl();
		RuntimeDelegate.setInstance(delegate);
	}

	public JaxRsRequestHandler(Class<A> applicationClass) {
		super();
		try {
			this.application = applicationClass.getConstructor().newInstance();
			this.router = new JaxRsRouter<A>(this.application);

		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Response handle(MiniRequest request, Container container)
			throws NotAllowedException, NotSupportedException, NotAcceptableException {

		try {
			final Method method = this.router.resolveHandlerMethod(request);
			final Class<?> clazz = method.getDeclaringClass();

			RuntimeDelegateImpl runtimeDelegate = (RuntimeDelegateImpl) RuntimeDelegate.getInstance();
			runtimeDelegate.setContextualData(MiniRequest.class, request);

			Response response = new InvokableInjector(method, container.getInstance(clazz)).inject(request).invoke();
			return response;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
