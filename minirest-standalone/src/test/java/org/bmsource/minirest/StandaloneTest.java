package org.bmsource.minirest;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bmsource.minirest.application.HelloApplication;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

public class StandaloneTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static HttpServer server = null;
	private static int port = 8080;

	private String host = "localhost";

	@BeforeClass
	public static void setUp() throws IOException {
		final HttpServerConfiguration sc = new HttpServerConfiguration();
		sc.setPort(port);
		server = new HttpServer(sc);
		server.createContext("test", HelloApplication.class);
		server.start();
	}

	@AfterClass
	public static void tearDown() throws IOException {
		server.stop();
	}

	@Test
	public void testStandaloneServer() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/test/hello/123/test/321");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String r = EntityUtils.toString(response.getEntity());

		Assert.assertNotNull(r);
		Assert.assertEquals(200, response.getStatusLine().getStatusCode());
		httpclient.close();
	}
}
