package org.bmsource.minirest.servlet;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.bmsource.minirest.MiniRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiniRequestBuilder {

	private static final Logger logger = LoggerFactory.getLogger(MiniRequestBuilder.class);

	private MiniRequest request = new MiniRequest();

	public static MiniRequest build(HttpServletRequest httpServletRequest) {
		MiniRequestBuilder builder = new MiniRequestBuilder();

		builder.request.setMethod(httpServletRequest.getMethod());
		builder.request.setAbsoluteLocation(httpServletRequest.getRequestURI());
		builder.request.setLocation(httpServletRequest.getRequestURI());
		builder.request.setContextPath(httpServletRequest.getContextPath());
		try {
			builder.request.setCharacterEncoding(httpServletRequest.getCharacterEncoding());
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		builder.request.setProtocol(httpServletRequest.getProtocol());

		// Cookies
		if (httpServletRequest.getCookies() != null) {

			javax.ws.rs.core.Cookie[] restCookies = new javax.ws.rs.core.Cookie[httpServletRequest.getCookies().length];
			int idx = 0;
			for (Cookie cookie : httpServletRequest.getCookies()) {
				javax.ws.rs.core.Cookie ck = new javax.ws.rs.core.Cookie(cookie.getName(), cookie.getValue(),
						cookie.getPath(), cookie.getDomain());
				restCookies[idx] = ck;
				idx++;
			}
			builder.request.setCookies(restCookies);
		}

		// Headers
		final List<String> names = Collections.list(httpServletRequest.getHeaderNames());
		for (String name : names) {
			final List<String> list = Collections.list(httpServletRequest.getHeaders(name));
			builder.request.addHeader(name, list.toArray(new String[list.size()]));
		}

		return builder.build();
	}

	public MiniRequest build() {
		return request;
	}
}
