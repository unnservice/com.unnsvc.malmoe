
package com.unnsvc.malmoe.mavenResolver;

import java.io.File;

import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.resolver.IRemoteResolver;
import com.unnsvc.malmoe.common.resolver.IRemoteResolverFactory;

public class MavenRemoteResolverFactory implements IRemoteResolverFactory {

	@Override
	public String getResolverId() {

		return "maven";
	}

	@Override
	public IRemoteResolver newRemoteResolver(IResolverConfig resolverConfig, File repositoryLocation) {

		return new MavenRemoteResolver(resolverConfig, repositoryLocation);
	}

}
