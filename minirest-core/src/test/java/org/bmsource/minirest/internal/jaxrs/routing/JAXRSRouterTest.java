package org.bmsource.minirest.internal.jaxrs.routing;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.bmsource.minirest.internal.ContainerRequest;
import org.bmsource.minirest.internal.jaxrs.routing.JaxRsRouter;
import org.junit.Test;

import junit.framework.Assert;

public class JAXRSRouterTest {

	private HelloApplication application = new HelloApplication();

	@Test
	public void testRouter() throws URISyntaxException {
		final JaxRsRouter<HelloApplication> router = new JaxRsRouter<HelloApplication>(application);

		ContainerRequest request = new ContainerRequest();
		request.setLocation("/first/1234/second/999");
		request.setMethod("GET");
		Method method = router.resolveHandlerMethod(request);
		Assert.assertEquals("helloId", method.getName());

		request = new ContainerRequest();
		request.setLocation("/first/1234/second/");
		request.setMethod("POST");
		method = router.resolveHandlerMethod(request);
		Assert.assertEquals("helloPost", method.getName());
	}

	public class HelloApplication extends Application {

		@Override
		public Set<Class<?>> getClasses() {
			Set<Class<?>> s = new HashSet<Class<?>>();
			s.add(Resource1.class);
			s.add(Resource2.class);
			return s;
		}
	}

	@Path("/first/{id}/second")
	public class Resource1 {

		@GET
		public Response hello() throws Exception {
			return Response.ok("ok 1").build();
		}

		@POST
		public Response helloPost() throws Exception {
			return Response.ok("ok 2").build();
		}

		@GET
		public Response helloId(@PathParam("id") String id) {
			return Response.ok("ok 3 " + id).build();
		}
	}

	@Path("/first/{id}/second/{id2}")
	public class Resource2 {

		@GET
		public Response helloId(@PathParam("id") String id) {
			return Response.ok("ok 3 " + id).build();
		}
	}
}
