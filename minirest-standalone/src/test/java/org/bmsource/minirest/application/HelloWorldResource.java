package org.bmsource.minirest.application;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ManagedBean
@Path("hello/{id}/test")
public class HelloWorldResource {

	@PathParam("id")
	private String id;

	@QueryParam("q")
	private String qp;

	@HeaderParam(HttpHeaders.CACHE_CONTROL)
	String cacheControl = null;

	@CookieParam("LSID")
	Cookie cookieLSID = null;

	@Inject
	private RequestScopedBean bean;

	public RequestScopedBean getBean() {
		return bean;
	}

	public void setBean(RequestScopedBean bean) {
		this.bean = bean;
	}

	@GET
	public Response hello() throws Exception {
		System.out.println(bean.getRequestId());
		return Response.ok("get simple").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloPost2() throws Exception {
		System.out.println(bean.getRequestId());
		return Response.ok("post consumes produces json").build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloPost3() throws Exception {
		System.out.println(bean.getRequestId());
		return Response.ok("post produces json").build();
	}

	@POST
	public Response helloPost() throws Exception {
		System.out.println(bean.getRequestId());
		return Response.ok("post simple").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response helloPost4() throws Exception {
		System.out.println(bean.getRequestId());
		return Response.ok("post consumes json").build();
	}

	@GET
	@Path("/{id1}")
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	public Response helloId(@PathParam("id1") String id1, @QueryParam("q") String q) {
		return Response.ok("get id1=" + id1 + ", id=" + id + ", q=" + q + ", qp=" + qp + " cache=" + cacheControl
				+ " cookie=" + cookieLSID.getValue()).header("SERVER", "XXXXXXXX").build();
	}
}
