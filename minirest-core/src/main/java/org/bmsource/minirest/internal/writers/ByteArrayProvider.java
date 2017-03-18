package org.bmsource.minirest.internal.writers;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@Produces("*/*")
@Consumes("*/*")
public class ByteArrayProvider implements MessageBodyWriter<byte[]> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return type.isArray() && type.getComponentType().equals(byte.class);
	}

	@Override
	public long getSize(byte[] bytes, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
		return bytes.length;
	}

	@Override
	public void writeTo(byte[] bytes, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException {
		entityStream.write(bytes);
	}
}
