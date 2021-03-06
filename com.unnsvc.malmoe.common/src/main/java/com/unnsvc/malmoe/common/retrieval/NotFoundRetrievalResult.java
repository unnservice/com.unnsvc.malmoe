
package com.unnsvc.malmoe.common.retrieval;

import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;

public class NotFoundRetrievalResult implements IRetrievalResult {

	private IResolvedRequest request;

	public NotFoundRetrievalResult(IResolvedRequest request) {

		this.request = request;
	}

	public IResolvedRequest getRequest() {

		return request;
	}

	@Override
	public String toString() {

		return "NotFoundRetrievalResult [request=" + request + "]";
	}
}
