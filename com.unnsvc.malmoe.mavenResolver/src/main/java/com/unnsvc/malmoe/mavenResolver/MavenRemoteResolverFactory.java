
package com.unnsvc.malmoe.mavenResolver;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.resolver.IRemoteResolver;
import com.unnsvc.malmoe.resolver.IRemoteResolverFactory;

public class MavenRemoteResolverFactory implements IRemoteResolverFactory {

	@Override
	public String getResolverId() {

		return "maven";
	}

	@Override
	public IRemoteResolver newRemoteResolver(IResolverConfig resolverConfig) {

		return new MavenRemoteResolver(resolverConfig);
	}

}
