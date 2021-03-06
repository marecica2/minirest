package org.bmsource.minirest.servlet;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import junit.framework.Assert;

public class MinirestServletTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static Server server;

	private static String host = "localhost";
	private static int port = 8080;

	@BeforeClass
	public static void setUp() throws Exception {
		server = new Server(port);
		String rootPath = new File(new File(".").getAbsolutePath()).getCanonicalPath();
		WebAppContext webapp = new WebAppContext();
		webapp.setWar(rootPath + "\\target\\minirest-servlet-0.0.1-SNAPSHOT.war");
		server.setHandler(webapp);
		server.start();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		server.stop();
	}

	@Test
	public void test() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/hello/123/test/321");
		httpGet.setHeader("Content-type", "text/html");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String r = EntityUtils.toString(response.getEntity());
		Assert.assertNotNull(r);

		logger.info(r);

		Assert.assertEquals(200, response.getStatusLine().getStatusCode());
		httpclient.close();
	}
}
