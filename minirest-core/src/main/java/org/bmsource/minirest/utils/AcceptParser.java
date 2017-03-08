package org.bmsource.minirest.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AcceptParser {

	private static class Charset implements Comparable<Charset> {
		private String value;
		private float q = 1.0F;

		private Charset(String value, float q) {
			this.value = value;
			this.q = q;
		}

		@Override
		public int compareTo(Charset charset) {
			if (this == charset)
				return 0;
			if (q == charset.q)
				return 0;
			if (q < charset.q)
				return 1;
			if (q > charset.q)
				return -1;
			return 0;
		}
	}

	private static Charset parseCharset(String charset) {
		String params = null;
		int idx = charset.indexOf(";");
		if (idx > -1) {
			params = charset.substring(idx + 1).trim();
			charset = charset.substring(0, idx);
		}
		float q = 1.0F;
		if (params != null && !params.equals("")) {
			HashMap<String, String> typeParams = new HashMap<String, String>();

			int start = 0;

			while (start < params.length()) {
				start = HeaderParameterParser.setParam(typeParams, params, start);
			}
			String qval = typeParams.get("q");
			if (qval != null) {
				q = Float.valueOf(qval);
			}
		}
		return new Charset(charset, q);
	}

	/**
	 * Return list sorted with first most preferred
	 *
	 * @param header
	 * @return
	 */
	public static List<String> parseAcceptHeader(String header) {
		ArrayList<Charset> set = new ArrayList<Charset>();
		String[] sets = header.split(",");
		for (String charset : sets) {
			set.add(parseCharset(charset.trim()));
		}
		Collections.sort(set);
		ArrayList<String> rtn = new ArrayList<String>();
		for (Charset c : set) {
			rtn.add(c.value);
		}
		return rtn;
	}

}
