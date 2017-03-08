package org.bmsource.minirest.internal.jaxrs.specimpl;

public interface HeaderValueProcessor {
	/**
	 * Convert an object to a header string. First try StringConverter, then
	 * HeaderDelegate, then object.toString()
	 *
	 * @param object
	 * @return
	 */
	String toHeaderString(Object object);
}
