package org.bmsource.minirest.internal.jaxrs.delegates;

import java.util.Locale;

import javax.ws.rs.ext.RuntimeDelegate;

import org.bmsource.minirest.utils.LocaleHelper;

public class LocaleDelegate implements RuntimeDelegate.HeaderDelegate<Locale> {
	@Override
	public Locale fromString(String value) throws IllegalArgumentException {
		if (value == null)
			throw new IllegalArgumentException();
		return LocaleHelper.extractLocale(value);
	}

	@Override
	public String toString(Locale value) {
		if (value == null)
			throw new IllegalArgumentException();
		return LocaleHelper.toLanguageString(value);
	}

}
