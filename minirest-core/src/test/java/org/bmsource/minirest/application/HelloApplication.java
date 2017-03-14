package org.bmsource.minirest.application;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class HelloApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(HelloWorldResource.class);
		return s;
	}
}
