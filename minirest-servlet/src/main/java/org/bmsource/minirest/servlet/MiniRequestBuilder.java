package org.bmsource.minirest.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.bmsource.minirest.MiniRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniRequestBuilder {

	private static final Logger logger = LoggerFactory.getLogger(MiniRequestBuilder.class);

	private MiniRequest request = new MiniRequest();

	public static MiniRequest build(HttpServletRequest httpServletRequest) throws IOException {
		MiniRequestBuilder builder = new MiniRequestBuilder();

		builder.request.setMethod(httpServletRequest.getMethod());
		builder.request.setAbsoluteLocation(httpServletRequest.getRequestURI());
		builder.request.setLocation(httpServletRequest.getRequestURI());
		builder.request.setContextPath(httpServletRequest.getContextPath());
		builder.request.setCharacterEncoding(httpServletRequest.getCharacterEncoding());
		builder.request.setProtocol(httpServletRequest.getProtocol());
		builder.request.setCookies(httpServletRequest.getCookies());
		builder.request.addHeader(httpServletRequest.getHeader());

		return builder.build();
	}

	public MiniRequest build() {
		return request;
	}
}
