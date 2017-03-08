package org.bmsource.minirest.internal;

import javax.ws.rs.core.Response;

import org.bmsource.minirest.MiniRequest;
import org.bmsource.minirest.internal.container.Container;

public interface RequestHandler {

	public Response handle(MiniRequest request, Container container);

}