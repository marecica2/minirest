package org.bmsource.minirest.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AnnotationUtils {

	public static <A extends Annotation> Set<String> getAnnotation(Method method, Class<A> annotationClass) {
		HashSet<String> methods = new HashSet<String>();
		for (Annotation annotation : method.getAnnotations()) {
			A a = annotation.annotationType().getAnnotation(annotationClass);
			if (a != null)
				try {
					methods.add((String) a.getClass().getMethod("value").invoke(a));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
		}
		if (methods.size() == 0)
			return null;
		return methods;
	}
}
