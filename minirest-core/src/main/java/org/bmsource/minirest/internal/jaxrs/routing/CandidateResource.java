package org.bmsource.minirest.internal.jaxrs.routing;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

import javax.ws.rs.Path;

public class CandidateResource {
	private Class<?> resourceClass;
	private Matcher matcher;
	private String convertedTemplate;

	private static final Comparator<CandidateResource> annotationComparator = (CandidateResource c1,
			CandidateResource c2) -> ((Integer) c1.resourceClass.getDeclaredAnnotation(Path.class).value().length())
					.compareTo(c2.resourceClass.getDeclaredAnnotation(Path.class).value().length());

	private static final Comparator<CandidateResource> regexComparator = (CandidateResource c1,
			CandidateResource c2) -> ((Integer) c1.matcher.groupCount()).compareTo(c2.matcher.groupCount());

	public static void sort(List<CandidateResource> list) {
		list.sort(annotationComparator.thenComparing(regexComparator).reversed());
	}

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
