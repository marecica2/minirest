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

import junit.framework.Assert;

public class MinirestServletTest {

	private static Server server;

	private static String host = "localhost";
	private static int port = 8080;

	@BeforeClass
	public static void setUp() throws Exception {
		server = new Server(port);

		String rootPath = MinirestServletTest.class.getClassLoader().getResource(".").toString();
		WebAppContext webapp = new WebAppContext();
		File f = new File(
				"d:/Programme/eclipse-mars-workspace/minirest/minirest-servlet/target/minirest-servlet-0.0.1-SNAPSHOT.war");
		System.err.println(f.exists());
		System.err.println(f.getAbsolutePath());

		webapp.setWar(
				"d:/Programme/eclipse-mars-workspace/minirest/minirest-servlet/target/minirest-servlet-0.0.1-SNAPSHOT.war");
		System.err.println("HEEEEEEEEEEEERE");
		System.err.println("HEEEEEEEEEEEERE");
		System.err.println("HEEEEEEEEEEEERE");
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
		// HttpGet httpGet = new HttpGet("http://" + host + ":" + port +
		// "/test/hello/123/test/321");
		HttpGet httpGet = new HttpGet("http://" + host + ":" + port + "/test");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		String r = EntityUtils.toString(response.getEntity());

		Assert.assertNotNull(r);
		Assert.assertEquals(200, response.getStatusLine().getStatusCode());
		httpclient.close();
	}
}
