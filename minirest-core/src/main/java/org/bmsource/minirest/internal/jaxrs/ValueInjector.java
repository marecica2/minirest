package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import org.bmsource.minirest.MiniRequest;

public interface ValueInjector {

	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field,
			MiniRequest request) throws IllegalAccessException;

	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			MiniRequest request);

}
