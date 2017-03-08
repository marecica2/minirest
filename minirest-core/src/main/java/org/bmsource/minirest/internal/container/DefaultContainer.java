package org.bmsource.minirest.internal.container;

public class DefaultContainer implements Container {

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public <T> T getInstance(Class<T> clazz) throws Exception {
		return clazz.newInstance();
	}
}
