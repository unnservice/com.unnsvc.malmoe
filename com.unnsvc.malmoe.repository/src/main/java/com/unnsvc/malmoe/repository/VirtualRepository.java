
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.repository.config.VirtualRepositoryConfig;

public class VirtualRepository implements IMalmoeRepository {

	private IAccessManager accessManager;

	public VirtualRepository(VirtualRepositoryConfig virtualConfig, IIdentityManager identityManager) {

		this.accessManager = new AccessManager(virtualConfig.getAccessConfig(), identityManager);
	}

	public IRetrievalResult retrieveModule(IRetrievalRequest request) {

		throw new UnsupportedOperationException("Virtual repository implemented");
	}
}
