package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.CookieParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.bmsource.minirest.MiniRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvokableInjector {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final Map<Class<? extends Annotation>, ValueInjector> extractors = new HashMap<>();

	static {
		extractors.put(PathParam.class, new PathParamInjector());
		extractors.put(QueryParam.class, new QueryParamInjector());
		extractors.put(HeaderParam.class, new HeaderParamInjector());
		extractors.put(CookieParam.class, new CookieParamInjector());
	}

	private Invokable<?> invokable;

	public InvokableInjector(Invokable<?> invokable) {
		this.invokable = invokable;
	}

	public InvokableInjector(Method method, Object instance) {
		this.invokable = new Invokable<Object>(method, instance);
	}

	public Invokable<?> inject(MiniRequest request) {
		injectResourceProperties(request);
		injectMethodParameters(request);
		return invokable;
	}

	private Invokable<?> injectResourceProperties(MiniRequest request) {
		extractors.forEach((annotation, injector) -> {
			final List<Field> fields = getAnnotatedResourceFields(annotation);
			fields.forEach(field -> {
				field.setAccessible(true);
				try {
					injector.injectResourceProperty(field.getAnnotation(annotation), invokable, field, request);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
				}
			});
		});

		return this.invokable;
	}

	private void injectMethodParameters(MiniRequest request) {
		final List<Parameter> parameters = Arrays.asList(this.invokable.getMethod().getParameters());
		parameters.forEach(parameter -> {
			extractors.forEach((annotation, injector) -> {
				if (parameter.isAnnotationPresent(annotation)) {
					injector.injectMethodParameter(parameter.getAnnotation(annotation), invokable, parameter, request);
				}
			});
		});
	}

	private List<Field> getAnnotatedResourceFields(Class<? extends Annotation> annotation) {
		List<Field> fields = new ArrayList<>();
		Arrays.asList(this.invokable.getResource().getClass().getDeclaredFields()).stream().forEach(field -> {
			field.setAccessible(true);
			if (field.isAnnotationPresent(annotation))
				fields.add(field);
		});
		return fields;
	}

}
