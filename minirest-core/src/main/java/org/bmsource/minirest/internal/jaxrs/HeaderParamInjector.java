package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import javax.ws.rs.HeaderParam;

import org.bmsource.minirest.MiniRequest;

public class HeaderParamInjector implements ValueInjector {

	@Override
	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field,
			MiniRequest request) throws IllegalAccessException {
		HeaderParam headerParam = (HeaderParam) annotation;
		field.setAccessible(true);
		field.set(invokable.getResource(), request.getHeader(headerParam.value()));
	}

	@Override
	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			MiniRequest request) {
		HeaderParam headerParam = (HeaderParam) annotation;
		invokable.addParameter(request.getHeader(headerParam.value()));
	}
}
