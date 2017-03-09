package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

import javax.ws.rs.CookieParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

import org.bmsource.minirest.MiniRequest;

public class CookieParamInjector extends AbstractInjector {

	@Override
	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field, MiniRequest request)
			throws IllegalAccessException {
		field.setAccessible(true);
		String cookieHeader = request.getHeader(HttpHeaders.COOKIE);
		if (cookieHeader != null)
			field.set(invokable.getResource(), Cookie.valueOf(cookieHeader));
	}

	@Override
	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			MiniRequest request) {
		CookieParam cookieParam = (CookieParam) annotation;
		invokable.addParameter(request.getHeader(cookieParam.value()));
	}
}
