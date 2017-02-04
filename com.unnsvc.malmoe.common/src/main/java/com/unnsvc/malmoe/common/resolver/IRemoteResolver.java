
package com.unnsvc.malmoe.common.resolver;

import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IRemoteResolver {

	public IRetrievalResult serveRequest(IResolvedArtifactRequest request) throws MalmoeException;

}
