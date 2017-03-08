package org.bmsource.minirest.internal.jaxrs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Invokable<R> {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Method method;

	private List<Object> parameterValues;

	private R resource;

	/**
	 * @param method
	 * @param resource
	 */
	public Invokable(Method method, R resource) {
		super();
		this.method = method;
		this.resource = resource;
		this.parameterValues = new ArrayList<>();
	}

	public Method getMethod() {
		return method;
	}

	public void addParameter(Object value) {
		this.parameterValues.add(value);
	}

	public R getResource() {
		return resource;
	}

	public Response invoke() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		logger.info("Invoking method {}.{}{}", resource.getClass().getCanonicalName(), method.getName(),
				parameterValues);

		return (Response) method.invoke(this.resource, this.parameterValues.toArray());
	}

}
