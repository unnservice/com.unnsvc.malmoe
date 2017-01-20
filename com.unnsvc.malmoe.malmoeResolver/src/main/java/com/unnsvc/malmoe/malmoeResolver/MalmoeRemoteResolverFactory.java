
package com.unnsvc.malmoe.malmoeResolver;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.resolver.IRemoteResolver;
import com.unnsvc.malmoe.resolver.IRemoteResolverFactory;

public class MalmoeRemoteResolverFactory implements IRemoteResolverFactory {

	@Override
	public String getResolverId() {

		return "malmoe";
	}

	@Override
	public IRemoteResolver newRemoteResolver(IResolverConfig resolverConfig) {

		return new MalmoeRemoteResolver(resolverConfig);
	}

}
