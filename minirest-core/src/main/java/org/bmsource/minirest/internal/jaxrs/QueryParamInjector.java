package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import javax.ws.rs.QueryParam;

import org.bmsource.minirest.MiniRequest;

public class QueryParamInjector implements ValueInjector {

	@Override
	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field,
			MiniRequest request) throws IllegalAccessException {
		QueryParam queryParam = (QueryParam) annotation;
		field.setAccessible(true);
		field.set(invokable.getResource(), extractQueryParameterValue(queryParam.value(), request.getQueryString()));
	}

	@Override
	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			MiniRequest request) {
		QueryParam queryParam = (QueryParam) annotation;
		invokable.addParameter(extractQueryParameterValue(queryParam.value(), request.getQueryString()));
	}

	private String extractQueryParameterValue(String queryParameter, String queryString) {
		if (queryString != null) {
			String[] pairs = queryString.split("&");
			return Arrays.asList(pairs).stream().filter(s -> s.startsWith(queryParameter)).findFirst().get()
					.split("=")[1];
		}
		return null;
	}
}
