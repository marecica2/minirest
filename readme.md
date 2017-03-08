Simple JAVAX RS WS Implementation

Example of use:

	import org.bmsource.HelloApplication;
	import org.bmsource.server.HttpServer;
	import org.bmsource.server.HttpServerConfiguration;
	
	public class Runner {
		public static void main(String... args) throws IOException {
			final HttpServerConfiguration sc = new HttpServerConfiguration();
			final HttpServer server = new HttpServer(sc);
			server.createContext("test", HelloApplication.class);
			server.start();
		}
	}
    
While HelloApplication is standard Java Restful Service 

	public class HelloApplication extends Application {

		@Override
		public Set<Class<?>> getClasses() {
			Set<Class<?>> s = new HashSet<Class<?>>();
			s.add(HelloWorldResource.class);
			return s;
		}
	}

	@Path("hello/{id}/test")
	public class HelloWorldResource {

		@PathParam("id")
		private String id;

		@GET
		@Path("/{id1}")
		@Consumes(MediaType.TEXT_HTML)
		@Produces(MediaType.TEXT_HTML)
		public Response helloId(@PathParam("id1") String id1, @QueryParam("q") String q) {
			return Response.ok("get id1=" + id1 + ", id=" + id + ", q=" + q + ", qp=" + qp + " cache=" + cacheControl
					+ " cookie=" + cookieLSID.getValue()).header("SERVER", "XXXXXXXX").build();
		}
	}
