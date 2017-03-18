package org.bmsource.minirest.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class ServerResponseWriter {

	public static void writeResponse(Response jaxrsResponse, final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		if (jaxrsResponse.getEntity() != null) {
			MediaType mt = null;
			Object o = jaxrsResponse.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
			if (o instanceof MediaType) {
				mt = (MediaType) o;
			} else if (o == null) {
				mt = MediaType.TEXT_HTML_TYPE;
			} else {
				mt = MediaType.valueOf(o.toString());
			}
			if (!mt.getParameters().containsKey(MediaType.CHARSET_PARAMETER)) {
				if ("text".equalsIgnoreCase(mt.getType()) || ("application".equalsIgnoreCase(mt.getType())
						&& mt.getSubtype().toLowerCase().startsWith("xml"))) {
					jaxrsResponse.getHeaders().putSingle(HttpHeaders.CONTENT_TYPE,
							mt.withCharset(StandardCharsets.UTF_8.toString()));
				}
			}
			response.setContentType(mt.getType());
		}
		response.setStatus(jaxrsResponse.getStatus());

		MultivaluedMap<String, Object> headers = jaxrsResponse.getHeaders();
		for (String header : headers.keySet()) {
			response.addHeader(header, jaxrsResponse.getHeaderString(header));
		}

		// MessageBodyWriter<byte[]> writer = new ByteArrayProvider();
		// writer.writeTo(((String) jaxrsResponse.getEntity()).getBytes(), null,
		// null, null, null, null, null);
		response.getOutputStream().write(((String) jaxrsResponse.getEntity()).getBytes());
	}

}
