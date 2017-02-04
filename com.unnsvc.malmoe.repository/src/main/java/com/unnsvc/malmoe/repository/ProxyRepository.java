
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IAccess;
import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IRepositoryManager;
import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.config.ProxyRepositoryConfig;
import com.unnsvc.malmoe.repository.retrieval.NotFoundRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.ServedRetrievalResult;

public class ProxyRepository implements IMalmoeRepository {

	private ProxyRepositoryConfig proxyConfig;
	private IRepositoryManager repositoryManager;
	private IAccessManager accessManager;

	public ProxyRepository(ProxyRepositoryConfig proxyConfig, IIdentityManager identityManager, IRepositoryManager repositoryManager) {

		this.proxyConfig = proxyConfig;
		this.accessManager = new AccessManager(proxyConfig.getAccessConfig(), identityManager);
		this.repositoryManager = repositoryManager;
	}

	@Override
	public IRetrievalResult serveRequest(IResolvedRequest request) throws MalmoeException {

		return accessManager.withPermissions(request, new IAccess<IRetrievalResult>() {

			public IRetrievalResult execute() throws MalmoeException {

				for (IReference ref : proxyConfig.getProxyChainConfig()) {

					IMalmoeRepository repo = repositoryManager.getRepository(ref.getRef());
					if (repo != null) {
						IRetrievalResult result = repo.serveRequest(request);
						if (result instanceof ServedRetrievalResult) {
							return result;
						}
					}
				}

				return new NotFoundRetrievalResult(request);
			}
		}, IMalmoeRepository.ACCESS_REPOSITORY_READ);
	}
}
