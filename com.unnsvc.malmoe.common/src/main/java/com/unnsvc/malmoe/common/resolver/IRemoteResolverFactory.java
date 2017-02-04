
package com.unnsvc.malmoe.common.resolver;

import java.io.File;

import com.unnsvc.malmoe.common.config.IResolverConfig;

public interface IRemoteResolverFactory {

	public String getResolverId();

	public IRemoteResolver newRemoteResolver(IResolverConfig resolverConfig, File repositoryLocation);
}
