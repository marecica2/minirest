package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import org.bmsource.minirest.internal.ContainerRequest;

public interface ValueInjector {

	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field,
			ContainerRequest request) throws IllegalAccessException;

	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			ContainerRequest request);

}
