
package com.unnsvc.malmoe.repository;

import java.util.ServiceLoader;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IRepositoriesConfig;
import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.config.ProxyRepositoryConfig;
import com.unnsvc.malmoe.repository.config.VirtualRepositoryConfig;
import com.unnsvc.malmoe.resolver.IRemoteResolver;
import com.unnsvc.malmoe.resolver.IRemoteResolverFactory;

public class RepositoryManager implements IRepositoryManager {

	private IIdentityManager identityManager;
	private IRepositoriesConfig repositoriesConfig;
	private ServiceLoader<IRemoteResolverFactory> resolvers;

	public RepositoryManager(IIdentityManager identityManager, IRepositoriesConfig repositoriesConfig) {

		this.identityManager = identityManager;
		this.repositoriesConfig = repositoriesConfig;
		this.resolvers = ServiceLoader.load(IRemoteResolverFactory.class);
	}

	@Override
	public IRetrievalResult retrieveModule(IRetrievalRequest request) throws MalmoeException {

		IMalmoeRepository repo = getRepository(request.getRepositoryName());
		return repo.retrieveModule(request);
	}

	@Override
	public IMalmoeRepository getRepository(String repositoryName) throws MalmoeException {

		for (IRepositoryConfig config : repositoriesConfig) {
			if (config.getRepositoryName().equals(repositoryName)) {

				if (config instanceof ProxyRepositoryConfig) {

					ProxyRepositoryConfig proxyConfig = (ProxyRepositoryConfig) config;
					return new ProxyRepository(proxyConfig, identityManager, this);
				} else if (config instanceof VirtualRepositoryConfig) {

					VirtualRepositoryConfig virtualConfig = (VirtualRepositoryConfig) config;
					IRemoteResolver resolver = getResolver(virtualConfig.getResolverConfig());
					return new VirtualRepository(virtualConfig, identityManager);
				}
			}
		}
		throw new MalmoeException("No such repository: " + repositoryName);
	}

	private IRemoteResolver getResolver(IResolverConfig resolverConfig) throws MalmoeException {

		for (IRemoteResolverFactory fact : resolvers) {
			if (fact.getResolverId().equals(resolverConfig.getResolverName())) {
				return fact.newRemoteResolver(resolverConfig);
			}
		}
		throw new MalmoeException("No such resolver: " + resolverConfig.getResolverName());
	}
}
