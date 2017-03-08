package org.bmsource.minirest.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerRequestBuilder {

	private static final Logger logger = LoggerFactory.getLogger(ContainerRequestBuilder.class);

	private ContainerRequest request = new ContainerRequest();

	private static final Map<String, BiFunction<ContainerRequest, String, String[]>> customheaderParsers = new HashMap<>();

	static {
		// Will parse Content-Type: text/html; charset=ISO-8859-4
		customheaderParsers.put(HttpHeaders.CONTENT_TYPE, (request, value) -> {
			if (!value.contains("; "))
				return new String[] { value };
			else {
				String[] vals = value.split("; ");
				try {
					request.setCharacterEncoding(vals[1].substring(vals[1].indexOf("=")));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				return new String[] { vals[0] };
			}
		});

		// Will parse Accept: text/plain; q=0.5, text/html,
		customheaderParsers.put(HttpHeaders.ACCEPT, (request, value) -> {
			if (!value.contains(", "))
				return new String[] { value };
			else {
				String[] vals = value.split(", ");
				return vals;
			}
		});

	}

	public static ContainerRequest build(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		ContainerRequestBuilder builder = new ContainerRequestBuilder();

		parseRequestLine(reader, builder);
		parseHeaders(reader, builder);

		return builder.build();
	}

	public ContainerRequest build() {
		return request;
	}

	private static void parseHeaders(BufferedReader reader, ContainerRequestBuilder builder) throws IOException {
		while (true) {
			final String[] line = reader.readLine().split(":\\s");
			if (line.length <= 1)
				break;
			final String header = line[0];
			String value = line[1];
			String[] headerValues = null;
			if (customheaderParsers.containsKey(header)) {
				headerValues = customheaderParsers.get(header).apply(builder.request, value);
			} else {
				headerValues = new String[] { value };
			}
			builder.addHeader(header, headerValues);
		}
	}

	private static void parseRequestLine(BufferedReader reader, ContainerRequestBuilder rb) throws IOException {
		String[] requestLine = reader.readLine().split("\\s");
		rb.setMethod(requestLine[0]);
		rb.setLocation(requestLine[1]);
		rb.setProtocol(requestLine[2]);
	}

	private ContainerRequestBuilder setMethod(String method) {
		request.setMethod(method);
		return this;
	}

	private ContainerRequestBuilder setLocation(String location) {
		request.setAbsoluteLocation(location);
		request.setLocation(location);
		return this;
	}

	private ContainerRequestBuilder setProtocol(String protocol) {
		request.setProtocol(protocol);
		return this;
	}

	private ContainerRequestBuilder addHeader(String key, String[] value) {
		request.addHeader(key, value);
		return this;
	}
}
