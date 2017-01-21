
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IAccess;
import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.config.VirtualRepositoryConfig;
import com.unnsvc.malmoe.resolver.IRemoteResolver;

/**
 * Location
 * 
 * @author noname
 *
 */
public class VirtualRepository implements IMalmoeRepository {

	private IAccessManager accessManager;
	private IRemoteResolver resolver;

	public VirtualRepository(VirtualRepositoryConfig virtualConfig, IIdentityManager identityManager, IRemoteResolver resolver) {

		this.accessManager = new AccessManager(virtualConfig.getAccessConfig(), identityManager);
		this.resolver = resolver;
	}

	@Override
	public IRetrievalResult serveRequest(IRetrievalRequest request) throws MalmoeException {

		return accessManager.withPermissions(request, new IAccess<IRetrievalResult>() {

			public IRetrievalResult execute() throws MalmoeException {
				
				return resolver.serveRequest(request);
			}
		}, IMalmoeRepository.ACCESS_REPOSITORY_READ);
	}
}
