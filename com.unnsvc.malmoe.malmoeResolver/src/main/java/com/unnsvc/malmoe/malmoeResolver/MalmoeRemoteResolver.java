
package com.unnsvc.malmoe.malmoeResolver;

import java.io.File;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.resolver.IRemoteResolver;

public class MalmoeRemoteResolver implements IRemoteResolver {

	public MalmoeRemoteResolver(IResolverConfig resolverConfig, File repositoryLocation) {

	}

	@Override
	public IRetrievalResult serveRequest(IRetrievalRequest request) {

		throw new UnsupportedOperationException("Not implemented");
	}

}
