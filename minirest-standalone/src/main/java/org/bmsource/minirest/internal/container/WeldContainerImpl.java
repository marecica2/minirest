package org.bmsource.minirest.internal.container;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class WeldContainerImpl implements Container {
	private Weld weld;
	private WeldContainer weldContainer;

	@Override
	public void start() {
		this.weld = new Weld();
		this.weldContainer = this.weld.initialize();
	}

	@Override
	public void stop() {
		this.weldContainer.shutdown();
	}

	@Override
	public <T> T getInstance(Class<T> clazz) {
		return weldContainer.select(clazz).get();
	}
}
