package org.bmsource.minirest.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	public static String convertURItoRegex(String uri) {
		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		// TODO normalize URI except regex placeholders
		// URLEncoder.encode(uri, "UTF-8");
		String encoded = uri.replaceAll("\\{.+?\\}", "([^/]+?)");
		if (encoded.endsWith("/")) {
			encoded.substring(0, encoded.length() - 1);
		}
		encoded += "(/.*)?";
		return encoded;
	}

	public static String convertURItoRegexNamed(String uri) {
		if (!uri.startsWith("/")) {
			uri = "/" + uri;
		}
		Pattern p = Pattern.compile("(\\{.+?\\})");
		Matcher m = p.matcher(uri);
		while (m.find()) {
			String replacement = "(?<" + m.group().substring(1, m.group().length() - 1) + ">[^/\\?]+?)";
			uri = m.replaceFirst(replacement);
			m.reset(uri);
		}

		if (uri.endsWith("/")) {
			uri.substring(0, uri.length() - 1);
		}
		uri += "(\\?.*|/.*)?";
		return uri;
	}
}
