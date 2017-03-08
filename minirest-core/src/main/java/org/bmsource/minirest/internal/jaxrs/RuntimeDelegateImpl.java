package org.bmsource.minirest.internal.jaxrs;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

import org.bmsource.minirest.internal.jaxrs.delegates.CacheControlDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.CookieHeaderDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.DateDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.EntityTagDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.LocaleDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.MediaTypeHeaderDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.NewCookieHeaderDelegate;
import org.bmsource.minirest.internal.jaxrs.delegates.UriHeaderDelegate;
import org.bmsource.minirest.internal.jaxrs.specimpl.HeaderValueProcessor;
import org.bmsource.minirest.internal.jaxrs.specimpl.ResponseBuilderImpl;

public class RuntimeDelegateImpl extends RuntimeDelegate implements HeaderValueProcessor {

	protected Map<Class<?>, HeaderDelegate> headerDelegates;

	protected static ThreadLocal<Map<Class<?>, Object>> context = new ThreadLocal<Map<Class<?>, Object>>() {
		@Override
		protected Map<Class<?>, Object> initialValue() {
			return new HashMap<>();
		}
	};

	@SuppressWarnings("unchecked")
	public <T> T getContextualData(Class<T> classType) {
		return (T) context.get().get(classType);
	}

	@SuppressWarnings("unchecked")
	public <T> T setContextualData(Class<T> classType, Object value) {
		return (T) context.get().put(classType, value);
	}

	protected void initialize() {
		context.set(new HashMap<>());
		headerDelegates = new ConcurrentHashMap<Class<?>, HeaderDelegate>();

		addHeaderDelegate(MediaType.class, new MediaTypeHeaderDelegate());
		addHeaderDelegate(NewCookie.class, new NewCookieHeaderDelegate());
		addHeaderDelegate(Cookie.class, new CookieHeaderDelegate());
		addHeaderDelegate(URI.class, new UriHeaderDelegate());
		addHeaderDelegate(EntityTag.class, new EntityTagDelegate());
		addHeaderDelegate(CacheControl.class, new CacheControlDelegate());
		addHeaderDelegate(Locale.class, new LocaleDelegate());
		// addHeaderDelegate(LinkHeader.class, new LinkHeaderDelegate());
		// addHeaderDelegate(javax.ws.rs.core.Link.class, new LinkDelegate());
		addHeaderDelegate(Date.class, new DateDelegate());
	}

	public RuntimeDelegateImpl() {
		initialize();
	}

	@Override
	public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> tClass) throws IllegalArgumentException {
		if (tClass == null)
			throw new IllegalArgumentException();

		HeaderDelegate<T> delegate = headerDelegates.get(tClass);
		if (delegate != null) {
			return delegate;
		}
		return null;
	}

	@Override
	public ResponseBuilder createResponseBuilder() {
		return new ResponseBuilderImpl();
	}

	@Override
	public UriBuilder createUriBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VariantListBuilder createVariantListBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T createEndpoint(Application application, Class<T> endpointType)
			throws IllegalArgumentException, UnsupportedOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Builder createLinkBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	protected Map<Class<?>, HeaderDelegate> getHeaderDelegates() {
		return headerDelegates;
	}

	public void addHeaderDelegate(Class<?> clazz, HeaderDelegate<?> header) {
		if (headerDelegates == null) {
			headerDelegates = new ConcurrentHashMap<Class<?>, HeaderDelegate>();
		}
		headerDelegates.put(clazz, header);
	}

	protected HeaderDelegate<?> getHeaderDelegate(Class<?> clazz) {
		return headerDelegates.get(clazz);
	}

	@Override
	public String toHeaderString(Object object) {
		if (object instanceof String)
			return (String) object;
		Class<?> aClass = object.getClass();

		// TODO
		// ParamConverter paramConverter = getParamConverter(aClass, null,
		// null);
		// if (paramConverter != null) {
		// return paramConverter.toString(object);
		// }
		// StringConverter converter = getStringConverter(aClass);
		// if (converter != null)
		// return converter.toString(object);

		HeaderDelegate delegate = getHeaderDelegate(aClass);
		if (delegate != null)
			return delegate.toString(object);
		else
			return object.toString();

	}
}
