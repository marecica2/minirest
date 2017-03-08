package org.bmsource.minirest.internal.jaxrs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.bmsource.minirest.MiniRequest;
import org.bmsource.minirest.utils.RegexUtils;

public class PathParamInjector extends AbstractInjector {

	@Override
	public void injectResourceProperty(Annotation annotation, Invokable<?> invokable, Field field,
			MiniRequest request) throws IllegalArgumentException, IllegalAccessException {
		PathParam pathParam = (PathParam) annotation;
		Path path = invokable.getResource().getClass().getAnnotation(Path.class);
		Object value = findPathSegmentValue(pathParam.value(), path.value(), request);
		field.setAccessible(true);
		field.set(invokable.getResource(), value);
	}

	@Override
	public void injectMethodParameter(Annotation annotation, Invokable<?> invokable, Parameter parameter,
			MiniRequest request) {
		final PathParam pathParam = (PathParam) annotation;
		final Path pathResource = invokable.getResource().getClass().getDeclaredAnnotation(Path.class);
		final Path pathMethod = invokable.getMethod().getAnnotation(Path.class);

		final String pathJoined = pathMethod != null ? pathResource.value() + "" + pathMethod.value()
				: pathResource.value();
		Object value = findPathSegmentValue(pathParam.value(), pathJoined, request);

		invokable.addParameter(value);
	}

	private Object findPathSegmentValue(String pathParam, String path, MiniRequest request) {

		Pattern p = Pattern.compile(RegexUtils.convertURItoRegexNamed(path));
		Matcher m = p.matcher(request.getNormalizedRelativePath().toString());
		if (m.matches()) {
			return m.group(pathParam);
		}
		return null;
	}

}
