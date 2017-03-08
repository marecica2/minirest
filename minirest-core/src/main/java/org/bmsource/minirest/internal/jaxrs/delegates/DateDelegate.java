package org.bmsource.minirest.internal.jaxrs.delegates;

import java.util.Date;

import javax.ws.rs.ext.RuntimeDelegate;

import org.bmsource.minirest.utils.DateUtil;

public class DateDelegate implements RuntimeDelegate.HeaderDelegate<Date> {
	@Override
	public Date fromString(String value) {
		if (value == null)
			throw new IllegalArgumentException();
		return DateUtil.parseDate(value);
	}

	@Override
	public String toString(Date value) {
		if (value == null)
			throw new IllegalArgumentException();
		return DateUtil.formatDate(value);
	}
}
