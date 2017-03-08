package org.bmsource.minirest.internal;

import java.io.OutputStream;
import java.io.PrintWriter;

import javax.ws.rs.core.MultivaluedMap;

public class ResponseBuilder {

	public static void build(OutputStream os, javax.ws.rs.core.Response response) {
		PrintWriter pw = new PrintWriter(os);
		buildResponseLine(response, pw);
		buildHeaderLines(response, pw);
		pw.println();
		if (response.getEntity() != null) {
			pw.print(response.getEntity().toString());
		}
		pw.flush();
	}

	private static void buildHeaderLines(javax.ws.rs.core.Response response, PrintWriter pw) {
		MultivaluedMap<String, Object> headers = response.getHeaders();
		headers.entrySet().stream().forEach(entry -> pw.println(entry.getKey() + ": " + entry.getValue()));
	}

	private static void buildResponseLine(javax.ws.rs.core.Response response, PrintWriter pw) {
		pw.println("HTTP/1.1 " + response.getStatus() + " " + response.getStatusInfo().getReasonPhrase());
	}

}
