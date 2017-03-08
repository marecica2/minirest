package org.bmsource.minirest.application;

import javax.faces.bean.ApplicationScoped;

@ApplicationScoped
public class RequestScopedBean {

	private String requestId;

	/**
	 * Request ID setter.
	 *
	 * @param requestId
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * Get me current request ID.
	 *
	 * @return request id.
	 */
	public String getRequestId() {
		return requestId + "aaaaaaaaaaaa";
	}
}
