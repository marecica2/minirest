package org.bmsource.minirest;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bmsource.minirest.internal.ContainerRequest;
import org.bmsource.minirest.internal.ContainerRequestBuilder;
import org.bmsource.minirest.internal.ContextChain;
import org.bmsource.minirest.internal.JaxRsRequestHandler;
import org.bmsource.minirest.internal.ResponseBuilder;
import org.bmsource.minirest.internal.cdi.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer implements Runnable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ServerSocket serverSocket;
	private final ExecutorService pool;
	private final HttpServerConfiguration serverConfiguration;
	private Thread serverThread;
	private boolean requestStop = false;
	private final ContextChain contexts;
	private final Container container;

	public HttpServer(HttpServerConfiguration sc) throws IOException {
		this.serverConfiguration = sc;
		this.serverSocket = new ServerSocket(sc.getPort());
		this.contexts = new ContextChain();
		this.pool = Executors.newFixedThreadPool(sc.getPoolSize());
		this.container = new Container();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HttpContext createContext(String path, Class<? extends Application> application) {
		JaxRsRequestHandler<?> handler = new JaxRsRequestHandler(application);
		logger.info("Context initialized for Application {} and listening on path \"/{}\"", application.getName(),
				path);
		return contexts.createContext(path, handler, container);
	}

	/**
	 * Starts the server
	 */
	public synchronized void start() {
		this.container.start();
		this.serverThread = new Thread(this, "SimpleHttpServer");
		serverThread.start();
	}

	/**
	 * Stops the server
	 *
	 * @throws IOException
	 */
	public synchronized void stop() throws IOException {
		this.requestStop = true;
		Socket socket = new Socket("localhost", serverConfiguration.getPoolSize());
		socket.close();
		this.container.stop();
		this.serverThread.interrupt();
	}

	@Override
	public void run() {
		try {
			logger.info("Server started and listening on port " + this.serverConfiguration.getPort());
			while (!this.requestStop) {
				pool.execute(new SocketConnectionHandler(serverSocket.accept()));
			}
			logger.info("Server shutting down...");
			this.serverSocket.close();
			pool.shutdown();
			logger.info("Server is stopped");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private class SocketConnectionHandler implements Runnable {
		private Socket socket;

		public SocketConnectionHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			handleConnection();
		}

		private void handleConnection() {
			OutputStream outputStream = null;

			try {
				final ContainerRequest request = ContainerRequestBuilder.build(socket.getInputStream());
				logger.info(request.toString());
				final HttpContext context = contexts.getHandlerContext(request);

				outputStream = socket.getOutputStream();
				if (context != null) {
					request.setContextPath(context.getContextPath());
					final Response response = context.handleRequest(request);
					ResponseBuilder.build(socket.getOutputStream(), response);
				} else {
					throw new NotFoundException();
				}

			} catch (NotFoundException e) {
				ResponseBuilder.build(outputStream, Response.status(Status.NOT_FOUND).build());

			} catch (NotAllowedException e) {
				ResponseBuilder.build(outputStream, Response.status(Status.METHOD_NOT_ALLOWED).build());

			} catch (NotSupportedException e) {
				ResponseBuilder.build(outputStream, Response.status(Status.UNSUPPORTED_MEDIA_TYPE).build());

			} catch (NotAcceptableException e) {
				ResponseBuilder.build(outputStream, Response.status(Status.NOT_ACCEPTABLE).build());

			} catch (IOException e) {
				e.printStackTrace();

			} finally {
				try {
					socket.getOutputStream().close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
