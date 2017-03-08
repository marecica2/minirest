package org.bmsource.minirest.internal.jaxrs.routing;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.bmsource.minirest.internal.ContainerRequest;
import org.bmsource.minirest.utils.AnnotationUtils;
import org.bmsource.minirest.utils.RegexUtils;

public class JaxRsRouter<A extends Application> {

	// private final Logger logger = LoggerFactory.getLogger(JAXRSRouter.class);

	private A application;

	public JaxRsRouter(A application) {
		this.application = application;
	}

	public Method resolveHandlerMethod(ContainerRequest request)
			throws NotAllowedException, NotSupportedException, NotAcceptableException {
		final URI inputURI = request.getNormalizedRelativePath();
		final URI uri = inputURI.normalize();
		final Set<CandidateResource> candidateResources = resolveCandidateRootResource(uri);

		if (candidateResources.isEmpty()) {
			throw new NotFoundException();
		}

		final List<CandidateResourceMethod> candidateMethods = findCandidateMethods(candidateResources);
		if (candidateMethods.size() == 0) {
			throw new NotFoundException();
		}

		List<CandidateResourceMethod> methods = identifyMethod(candidateMethods, request);
		if (methods.isEmpty()) {
			throw new NotFoundException();
		}

		// TODO Handle multiple methods

		Method method = methods.get(0).getMethod();
		return method;
	}

	private Set<CandidateResource> resolveCandidateRootResource(URI uri) {
		final Set<Class<?>> resourceClasses = application.getClasses();
		final List<CandidateResource> candidateResourceClasses = new ArrayList<>();

		for (Class<?> resourceClass : resourceClasses) {
			final Path path = resourceClass.getDeclaredAnnotation(Path.class);
			final String resourceClassUriTemplate = RegexUtils.convertURItoRegex(path.value());
			final Pattern p = Pattern.compile(resourceClassUriTemplate);

			final Matcher matcher = p.matcher(uri.toString());
			if (matcher.matches()) {
				final CandidateResource cr = new CandidateResource(resourceClass, resourceClassUriTemplate, matcher);
				if (cr.getLastMatchingGroup() != null && !cr.getLastMatchingGroup().equals("/")
						&& !cr.hasSubResourceMethod()) {

				} else {
					candidateResourceClasses.add(cr);
				}
			}
		}

		Collections.sort(candidateResourceClasses);
		Collections.reverse(candidateResourceClasses);
		return new LinkedHashSet<CandidateResource>(candidateResourceClasses);
	}

	private List<CandidateResourceMethod> findCandidateMethods(Set<CandidateResource> candidateResources) {

		final String capturingGroup = candidateResources.iterator().next().getLastMatchingGroup();
		final List<CandidateResourceMethod> candidateMethods = new ArrayList<>();

		if (capturingGroup == null || capturingGroup.equals("/")) {
			for (CandidateResource cr : candidateResources) {
				Method[] crMethods = cr.getResourceClass().getDeclaredMethods();
				for (Method method : crMethods) {
					if (!method.isAnnotationPresent(Path.class)) {
						candidateMethods.add(new CandidateResourceMethod(method, null, null, cr.getResourceClass()));
					}
				}
			}
			return candidateMethods;
		} else {

			for (CandidateResource cr : candidateResources) {
				Method[] crMethods = cr.getResourceClass().getDeclaredMethods();
				for (Method method : crMethods) {
					if (method.isAnnotationPresent(Path.class)) {
						final Path path = method.getAnnotation(Path.class);
						final String resourcePathUriTemplate = RegexUtils.convertURItoRegex(path.value());

						final Pattern p = Pattern.compile(resourcePathUriTemplate);
						final Matcher matcher = p.matcher(capturingGroup.toString());
						if (matcher.matches()) {
							final CandidateResourceMethod crMethod = new CandidateResourceMethod(method,
									resourcePathUriTemplate, matcher, cr.getResourceClass());
							if (crMethod.getLastMatchingGroup() == null
									|| crMethod.getLastMatchingGroup().equals("/")) {
								candidateMethods.add(crMethod);
							}
						}
					}
				}
			}
			Collections.sort(candidateMethods);
			Collections.reverse(candidateMethods);
		}
		return candidateMethods;
	}

	private List<CandidateResourceMethod> identifyMethod(List<CandidateResourceMethod> candidateMethods,
			ContainerRequest request) throws NotAllowedException, NotSupportedException, NotAcceptableException {
		boolean isMethodSupported = false;

		// Content-type
		String requestMediaType = request.getContentType();
		boolean isRequestMediaTypeSupported = false;

		// Accept
		String responseMediaType = request.getAccept();
		boolean isResponseMediaTypeSupported = false;

		for (Iterator<CandidateResourceMethod> iterator = candidateMethods.iterator(); iterator.hasNext();) {
			final CandidateResourceMethod method = iterator.next();
			boolean remove = false;

			if (!checkMethodSupport(request, method))
				remove = true;
			else {
				isMethodSupported = true;
			}

			if (requestMediaType != null) {
				if (!isContentTypeSupported(request, method)) {
					remove = true;
				} else {
					isRequestMediaTypeSupported = true;
				}
			}

			if (responseMediaType != null) {
				if (!isAcceptSupported(request, method)) {
					remove = true;
				} else {
					isResponseMediaTypeSupported = true;
				}
			}

			if (remove) {
				iterator.remove();
			}

		}

		if (responseMediaType != null && !isResponseMediaTypeSupported && candidateMethods.isEmpty())
			throw new NotAcceptableException(Response.status(Status.NOT_ACCEPTABLE).build());

		if (requestMediaType != null && !isRequestMediaTypeSupported && candidateMethods.isEmpty())
			throw new NotSupportedException(Response.status(Status.UNSUPPORTED_MEDIA_TYPE).build());

		if (!isMethodSupported && candidateMethods.isEmpty())
			throw new NotAllowedException(Response.status(Status.METHOD_NOT_ALLOWED).build());

		return candidateMethods;
	}

	private boolean isContentTypeSupported(ContainerRequest request, CandidateResourceMethod resourceMethod) {
		if (MediaType.WILDCARD.equals(request.getContentType()))
			return true;

		Consumes consumes = resourceMethod.getMethod().getAnnotation(Consumes.class);
		if (consumes == null) {
			consumes = resourceMethod.getMethod().getDeclaringClass().getAnnotation(Consumes.class);
		}

		if (consumes != null
				&& new HashSet<String>(Arrays.asList(consumes.value())).contains(request.getContentType())) {
			return true;
		}

		return false;
	}

	private boolean isAcceptSupported(ContainerRequest request, CandidateResourceMethod resourceMethod) {
		if (MediaType.WILDCARD.equals(request.getAccept()))
			return true;

		Produces produces = resourceMethod.getMethod().getAnnotation(Produces.class);
		if (produces == null) {
			produces = resourceMethod.getMethod().getDeclaringClass().getAnnotation(Produces.class);
		}

		if (produces != null && new HashSet<String>(Arrays.asList(produces.value())).contains(request.getAccept())) {
			return true;
		}

		return false;
	}

	private boolean checkMethodSupport(ContainerRequest request, final CandidateResourceMethod resourceMethod) {
		final Set<String> httpMethods = AnnotationUtils.getAnnotation(resourceMethod.getMethod(), HttpMethod.class);
		if (httpMethods == null || httpMethods.isEmpty() || !httpMethods.contains(request.getMethod())) {
			return false;
		}
		return true;
	}

}
