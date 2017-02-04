
package com.unnsvc.malmoe.malmoeResolver;

import java.io.File;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.resolver.IRemoteResolver;
import com.unnsvc.malmoe.common.resolver.IRemoteResolverFactory;

public class MalmoeRemoteResolverFactory implements IRemoteResolverFactory {

	@Override
	public String getResolverId() {

		return "malmoe";
	}

	@Override
	public IRemoteResolver newRemoteResolver(IResolverConfig resolverConfig, File repositoryLocation) {

		return new MalmoeRemoteResolver(resolverConfig, repositoryLocation);
	}

}
