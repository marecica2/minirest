package org.bmsource.minirest.internal;

import javax.ws.rs.core.Response;

import org.bmsource.minirest.internal.cdi.Container;

public interface RequestHandler {

	public Response handle(ContainerRequest request, Container container);

}