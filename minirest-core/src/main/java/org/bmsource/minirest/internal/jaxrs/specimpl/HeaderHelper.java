package org.bmsource.minirest.internal.jaxrs.specimpl;

import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

public class HeaderHelper {
	@SuppressWarnings(value = "unchecked")
	public static void setAllow(MultivaluedMap headers, String[] methods) {
		if (methods == null) {
			headers.remove("Allow");
			return;
		}
		StringBuilder builder = new StringBuilder();
		boolean isFirst = true;
		for (String l : methods) {
			if (isFirst) {
				isFirst = false;
			} else {
				builder.append(", ");
			}
			builder.append(l);
		}
		headers.putSingle("Allow", builder.toString());
	}

	@SuppressWarnings(value = "unchecked")
	public static void setAllow(MultivaluedMap headers, Set<String> methods) {
		if (methods == null) {
			headers.remove("Allow");
			return;
		}
		StringBuilder builder = new StringBuilder();
		boolean isFirst = true;
		for (String l : methods) {
			if (isFirst) {
				isFirst = false;
			} else {
				builder.append(", ");
			}
			builder.append(l);
		}
		headers.putSingle("Allow", builder.toString());
	}

}
