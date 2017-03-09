package org.bmsource.minirest;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bmsource.minirest.application.HelloApplication;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;
import junit.framework.TestCase;

public class StandaloneTest extends TestCase {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private HttpServer server = null;
	private int port = 8080;
	private String host = "localhost";

	@Override
	public void setUp() throws IOException {
		final HttpServerConfiguration sc = new HttpServerConfiguration();
		sc.setPort(port);
		server = new HttpServer(sc);
		server.createContext("test", HelloApplication.class);
		server.start();
	}

	@Override
	public void tearDown() throws IOException {
		server.stop();
	}

	@Test
	public void standaloneServerTest() throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/test/hello/123/test/321");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String r = EntityUtils.toString(response.getEntity());

		Assert.assertNotNull(r);
		Assert.assertEquals(200, response.getStatusLine().getStatusCode());
		httpclient.close();
	}
}
