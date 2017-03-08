package org.bmsource.minirest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bmsource.minirest.application.HelloApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StandaloneTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpServer server = null;
	private int port = 8080;
	private String host = "localhost";

	@Before
	public void setup() throws IOException {
		final HttpServerConfiguration sc = new HttpServerConfiguration();
		sc.setPort(port);
		server = new HttpServer(sc);
		server.createContext("test", HelloApplication.class);
		server.start();
	}

	@After
	public void tearDown() throws IOException {
		server.stop();
	}

	@Test
	public void startHttpServer() throws Exception {

		URL url = new URL("http://" + host + ":" + port + "/test/hello/123/test/321?q=qqqq");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "text/html");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();

	}

}
