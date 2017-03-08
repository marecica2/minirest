package org.bmsource.minirest.internal.jaxrs.routing;

import java.lang.reflect.Method;
import java.util.regex.Matcher;

import javax.ws.rs.Path;

public class CandidateResource implements Comparable<CandidateResource> {
	private Class<?> resourceClass;
	private Matcher matcher;
	private String convertedTemplate;

	public Class<?> getResourceClass() {
		return resourceClass;
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
	public CandidateResource(Class<?> resourceClass, String template, Matcher matcher) {
		super();
		this.resourceClass = resourceClass;
		this.convertedTemplate = template;
		this.matcher = matcher;
	}

	public boolean hasSubResourceMethod() {
		final Method[] methods = resourceClass.getMethods();
		for (final Method method : methods) {
			if (method.isAnnotationPresent(Path.class)) {
				return true;
			}
		}
		return false;
	}

	public String getConvertedTemplate() {
		return convertedTemplate;
	}

	@Override
	public int compareTo(CandidateResource resourceClass2) {
		final Path path1 = this.resourceClass.getDeclaredAnnotation(Path.class);
		final Path path2 = resourceClass2.getResourceClass().getDeclaredAnnotation(Path.class);
		if (path1.value().length() > path2.value().length()) {
			return 1;
		} else if (path1.value().length() < path2.value().length()) {
			return -1;
		} else if (path1.value().length() == path2.value().length()) {
			if (this.matcher.groupCount() > resourceClass2.getMatcher().groupCount()) {
				return 1;
			} else if (this.matcher.groupCount() > resourceClass2.getMatcher().groupCount()) {
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
		CandidateResource other = (CandidateResource) obj;
		if (convertedTemplate == null) {
			if (other.convertedTemplate != null)
				return false;
		} else if (!convertedTemplate.equals(other.convertedTemplate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return resourceClass.getSimpleName() + " @Path("
				+ this.getResourceClass().getDeclaredAnnotation(Path.class).value() + ")";
	}

}
