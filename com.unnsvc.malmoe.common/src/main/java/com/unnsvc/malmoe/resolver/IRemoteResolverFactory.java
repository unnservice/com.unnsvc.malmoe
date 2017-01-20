
package com.unnsvc.malmoe.resolver;

import com.unnsvc.malmoe.common.config.IResolverConfig;

public interface IRemoteResolverFactory {

	public String getResolverId();

	public IRemoteResolver newRemoteResolver(IResolverConfig resolverConfig);
}
