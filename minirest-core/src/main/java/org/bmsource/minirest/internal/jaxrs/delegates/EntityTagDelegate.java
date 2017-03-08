package org.bmsource.minirest.internal.jaxrs.delegates;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.ext.RuntimeDelegate;

public class EntityTagDelegate implements RuntimeDelegate.HeaderDelegate<EntityTag> {
	@Override
	public EntityTag fromString(String value) throws IllegalArgumentException {
		if (value == null)
			throw new IllegalArgumentException();
		boolean weakTag = false;
		if (value.startsWith("W/")) {
			weakTag = true;
			value = value.substring(2);
		}
		if (value.startsWith("\"")) {
			value = value.substring(1);
		}
		if (value.endsWith("\"")) {
			value = value.substring(0, value.length() - 1);
		}
		return new EntityTag(value, weakTag);
	}

	@Override
	public String toString(EntityTag value) {
		String weak = value.isWeak() ? "W/" : "";
		return weak + '"' + value.getValue() + '"';
	}

}
