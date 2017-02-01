
package com.unnsvc.malmoe.frontend.resolved;

import com.unnsvc.malmoe.frontend.IResolvedRequest;

public abstract class AbstractRepositoryResolvedRequest implements IResolvedRequest {

	private String repositoryId;

	public AbstractRepositoryResolvedRequest(String repositoryId) {

		this.repositoryId = repositoryId;
	}

	public String getRepositoryId() {

		return repositoryId;
	}
}
