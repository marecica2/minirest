package org.bmsource.minirest.internal.container;

public interface Container {

	void start();

	void stop();

	<T> T getInstance(Class<T> clazz) throws Exception;

}