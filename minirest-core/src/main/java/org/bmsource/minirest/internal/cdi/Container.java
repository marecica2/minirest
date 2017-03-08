package org.bmsource.minirest.internal.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Container {
	private Weld weld;
	private WeldContainer weldContainer;

	public void start() {
		this.weld = new Weld();
		this.weldContainer = this.weld.initialize();
	}

	public void stop() {
		this.weldContainer.shutdown();
		this.weld.shutdown();
	}

	public <T> T getInstance(Class<T> clazz) {
		return weldContainer.select(clazz).get();
	}
}
