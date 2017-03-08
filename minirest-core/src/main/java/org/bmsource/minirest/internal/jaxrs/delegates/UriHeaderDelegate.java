package org.bmsource.minirest.internal.jaxrs.delegates;

import java.net.URI;

import javax.ws.rs.ext.RuntimeDelegate;

public class UriHeaderDelegate implements RuntimeDelegate.HeaderDelegate {
	@Override
	public Object fromString(String value) throws IllegalArgumentException {
		if (value == null)
			throw new IllegalArgumentException();
		return URI.create(value);
	}

	@Override
	public String toString(Object value) {
		if (value == null)
			throw new IllegalArgumentException();
		URI uri = (URI) value;
		return uri.toASCIIString();
	}
}
