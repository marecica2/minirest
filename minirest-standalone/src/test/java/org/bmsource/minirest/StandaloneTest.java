package org.bmsource.minirest;

import java.io.IOException;

import org.bmsource.minirest.application.HelloApplication;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandaloneTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Before
	public void setup() throws IOException {
		final HttpServerConfiguration sc = new HttpServerConfiguration();
		sc.setPort(8080);
		final HttpServer server = new HttpServer(sc);
		server.createContext("test", HelloApplication.class);
		server.start();
	}

	@Test
	public void startHttpServer() {
		logger.info("Server started");
	}

}
