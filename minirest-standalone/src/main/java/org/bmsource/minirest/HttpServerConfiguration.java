package org.bmsource.minirest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerConfiguration {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private int port = 8080;
	private int poolSize = 20;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

}
