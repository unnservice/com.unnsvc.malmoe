
package com.unnsvc.malmoe.resolver;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IRemoteResolver {

	public IRetrievalResult serveRequest(IRetrievalRequest request) throws MalmoeException;

}
