package org.bmsource.minirest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;

public class MiniRequest {

	private String method;

	private String absoluteLocation;

	private String location;

	private String contextPath;

	private String protocol = "HTTP/1.1";

	private String scheme = "http";

	private String encoding = StandardCharsets.ISO_8859_1.name();

	private final Map<String, String[]> headers = new HashMap<>();

	private Cookie[] cookies = null;

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
		if (contextPath != null) {
			this.location = this.absoluteLocation.substring(contextPath.length());
		}
	}

	public String getContextPath() {
		return this.contextPath;
	}

	public void setAbsoluteLocation(String absoluteLocation) {
		this.absoluteLocation = absoluteLocation;
	}

	public String getAbsoluteLocation() {
		return this.absoluteLocation;
	}

	public URI getLocation() {
		try {
			return new URI(this.location);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void addHeader(String key, String[] value) {
		this.headers.put(key, value);
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;

	}

	public String getContentType() {
		if (headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
			return this.headers.get(HttpHeaders.CONTENT_TYPE)[0];
		}
		return null;
	}

	public String getAccept() {
		if (headers.containsKey(HttpHeaders.ACCEPT)) {
			return this.headers.get(HttpHeaders.ACCEPT)[0];
		}
		return null;
	}

	// public ServletInputStream getInputStream() throws IOException {
	// return null;
	// }

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return this.method;
	}

	public String getRelativePath() {
		return this.location;
	}

	public URI getNormalizedRelativePath() {
		try {
			return new URI(this.location).normalize();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "Request [method=" + method + ", location=" + absoluteLocation + ", protocol=" + protocol + "]";
	}

	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration<String> getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getCharacterEncoding() {
		return this.encoding;
	}

	public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
		this.encoding = env;
	}

	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getContentLengthLong() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getParameter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration<String> getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getProtocol() {
		return this.protocol;
	}

	public String getScheme() {
		return this.scheme;
	}

	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setAttribute(String name, Object o) {
		// TODO Auto-generated method stub

	}

	public void removeAttribute(String name) {
		// TODO Auto-generated method stub

	}

	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	public Enumeration<Locale> getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	// public RequestDispatcher getRequestDispatcher(String path) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public String getRealPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	// public ServletContext getServletContext() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public AsyncContext startAsync() throws IllegalStateException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public AsyncContext startAsync(ServletRequest servletRequest,
	// ServletResponse servletResponse)
	// throws IllegalStateException {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public boolean isAsyncStarted() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAsyncSupported() {
		// TODO Auto-generated method stub
		return false;
	}

	// public AsyncContext getAsyncContext() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public DispatcherType getDispatcherType() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}

	public Cookie[] getCookies() {
		return this.cookies;
	}

	public long getDateHeader(String name) {
		return Long.parseLong(getHeader(name));
	}

	public String getHeader(String name) {
		if (this.headers.containsKey(name)) {
			return this.headers.get(name)[0];
		}
		return null;
	}

	public Enumeration<String> getHeaders(String name) {
		return Collections.enumeration(Arrays.asList(this.headers.get(name)));
	}

	public String[] getHeadersAsList(String name) {
		return this.headers.get(name);
	}

	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(this.headers.keySet());
	}

	public int getIntHeader(String name) {
		return Integer.parseInt(getHeader(name));
	}

	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getQueryString() {
		if (this.location.contains("?")) {
			String queryString = this.location.substring(this.location.indexOf("?") + 1);
			if (queryString.contains("#")) {
				queryString = queryString.substring(0, queryString.indexOf("#"));
			}
			return queryString;
		} else {
			return null;
		}
	}

	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	// public HttpSession getSession(boolean create) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public HttpSession getSession() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public String changeSessionId() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}

	// public Collection<Part> getParts() throws IOException, ServletException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// public Part getPart(String name) throws IOException, ServletException {
	// // TODO Auto-generated method stub
	// return null;
	// }

}
