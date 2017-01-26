
package com.unnsvc.malmoe.repository.retrieval;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;

public class NotFoundRetrievalResult implements IRetrievalResult {

	private IRetrievalRequest request;

	public NotFoundRetrievalResult(IRetrievalRequest request) {

		this.request = request;
	}

	public IRetrievalRequest getRequest() {

		return request;
	}

}
