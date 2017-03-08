package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import javax.ws.rs.CookieParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

import org.bmsource.minirest.internal.ContainerRequest;

public class CookieParamInjector extends AbstractInjector {

	@Override
	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field,
			ContainerRequest request) throws IllegalAccessException {
		CookieParam cookieParam = (CookieParam) annotation;
		field.setAccessible(true);
		field.set(invokable.getResource(), Cookie.valueOf(request.getHeader(HttpHeaders.COOKIE)));
	}

	@Override
	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			ContainerRequest request) {
		CookieParam cookieParam = (CookieParam) annotation;
		invokable.addParameter(request.getHeader(cookieParam.value()));
	}
}
