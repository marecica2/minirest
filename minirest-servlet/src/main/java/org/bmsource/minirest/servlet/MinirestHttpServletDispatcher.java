package org.bmsource.minirest.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MinirestHttpServletDispatcher extends HttpServlet {

	private static final long serialVersionUID = 5461151992035823385L;

	protected ServletContainerDispatcher servletContainerDispatcher;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		servletContainerDispatcher = new ServletContainerDispatcher(servletConfig);
	}

	@Override
	public void destroy() {
		super.destroy();
		servletContainerDispatcher.destroy();
	}

	@Override
	protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws ServletException, IOException {
		service(httpServletRequest.getMethod(), httpServletRequest, httpServletResponse);
	}

	public void service(String httpMethod, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		servletContainerDispatcher.service(httpMethod, request, response);
	}

}
