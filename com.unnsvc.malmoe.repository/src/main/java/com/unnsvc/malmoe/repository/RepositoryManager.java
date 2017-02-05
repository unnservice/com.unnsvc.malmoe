
package com.unnsvc.malmoe.repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IRepositoriesConfig;
import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.exceptions.NotFoundMalmoeException;
import com.unnsvc.malmoe.common.resolver.IRemoteResolver;
import com.unnsvc.malmoe.common.resolver.IRemoteResolverFactory;
import com.unnsvc.malmoe.repository.config.ProxyRepositoryConfig;
import com.unnsvc.malmoe.repository.config.VirtualRepositoryConfig;

public class RepositoryManager implements IRepositoryManager {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File workspaceLocation;
	private IIdentityManager identityManager;
	private IRepositoriesConfig repositoriesConfig;
	private ServiceLoader<IRemoteResolverFactory> resolvers;

	public RepositoryManager(File workspaceLocation, IIdentityManager identityManager, IRepositoriesConfig repositoriesConfig) {

		this.workspaceLocation = workspaceLocation;
		this.identityManager = identityManager;
		this.repositoriesConfig = repositoriesConfig;
		this.resolvers = ServiceLoader.load(IRemoteResolverFactory.class, IRemoteResolverFactory.class.getClassLoader());
	}

	@Override
	public IRetrievalResult serveRequest(IResolvedRequest request) throws MalmoeException {

		IMalmoeRepository repo = getRepository(request.getRepositoryId());
		return repo.serveRequest(request);
	}

	@Override
	public IMalmoeRepository getRepository(String repositoryName) throws MalmoeException {

		for (IRepositoryConfig config : repositoriesConfig) {
			if (config.getRepositoryName().equals(repositoryName)) {

				if (config instanceof ProxyRepositoryConfig) {

					log.debug("Serving using repository: " + repositoryName + " of type proxy");

					ProxyRepositoryConfig proxyConfig = (ProxyRepositoryConfig) config;
					return new ProxyRepository(proxyConfig, identityManager, this);
				} else if (config instanceof VirtualRepositoryConfig) {

					VirtualRepositoryConfig virtualConfig = (VirtualRepositoryConfig) config;

					File storageLocation = new File(workspaceLocation, "storage");
					File repositoryLocation = new File(storageLocation, repositoryName);
					IRemoteResolver resolver = getResolver(repositoryLocation, virtualConfig.getResolverConfig());

					log.debug("Serving using repository: " + repositoryName + " of type virtual, location: " + repositoryLocation);

					return new VirtualRepository(virtualConfig, repositoryLocation, identityManager, resolver);
				}
			}
		}

		throw new NotFoundMalmoeException("No such repository: " + repositoryName);
	}

	private IRemoteResolver getResolver(File resolverLocation, IResolverConfig resolverConfig) throws MalmoeException {

		for (IRemoteResolverFactory fact : resolvers) {
			if (fact.getResolverId().equals(resolverConfig.getResolverName())) {
				return fact.newRemoteResolver(resolverConfig, resolverLocation);
			}
		}
		throw new MalmoeException("No such resolver: " + resolverConfig.getResolverName());
	}

	@Override
	public List<String> getRepositoryNames() {

		List<String> repositoryNames = new ArrayList<String>();
		for (IRepositoryConfig config : repositoriesConfig) {

			repositoryNames.add(config.getRepositoryName());
		}
		return repositoryNames;
	}
}
