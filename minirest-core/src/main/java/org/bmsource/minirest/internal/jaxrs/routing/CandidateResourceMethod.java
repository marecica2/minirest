package org.bmsource.minirest.internal.jaxrs.routing;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

import javax.ws.rs.Path;

public class CandidateResourceMethod implements Comparable<CandidateResourceMethod> {
	private Method method;
	private Matcher matcher;
	private String convertedTemplate;
	private Class<?> resourceClass;

	public Method getMethod() {
		return method;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public String getLastMatchingGroup() {
		return matcher.group(matcher.groupCount());
	}

	/**
	 * @param resourceClass
	 * @param matcher
	 */
	public CandidateResourceMethod(Method method, String template, Matcher matcher, Class<?> resourceClass) {
		super();
		this.method = method;
		this.convertedTemplate = template;
		this.matcher = matcher;
		this.resourceClass = resourceClass;
	}

	public String getConvertedTemplate() {
		return convertedTemplate;
	}

	@Override
	public int compareTo(CandidateResourceMethod resourceMethod) {
		final Path path1 = this.method.getDeclaredAnnotation(Path.class);
		final Path path2 = resourceMethod.getMethod().getDeclaredAnnotation(Path.class);
		if (path1.value().length() > path2.value().length()) {
			return 1;
		} else if (path1.value().length() < path2.value().length()) {
			return -1;
		} else if (path1.value().length() == path2.value().length()) {
			if (this.matcher.groupCount() > resourceMethod.getMatcher().groupCount()) {
				return 1;
			} else if (this.matcher.groupCount() > resourceMethod.getMatcher().groupCount()) {
				return -1;
			}
		}
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((convertedTemplate == null) ? 0 : convertedTemplate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CandidateResourceMethod other = (CandidateResourceMethod) obj;
		if (convertedTemplate == null) {
			if (other.convertedTemplate != null)
				return false;
		} else if (!convertedTemplate.equals(other.convertedTemplate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CandidateResourceMethod [method=" + method.getName() + "]";
	}

}
