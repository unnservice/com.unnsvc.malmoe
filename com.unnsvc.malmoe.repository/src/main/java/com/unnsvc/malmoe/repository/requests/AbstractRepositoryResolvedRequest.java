
package com.unnsvc.malmoe.repository.requests;

import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IUser;

public abstract class AbstractRepositoryResolvedRequest implements IResolvedRequest {

	private IUser user;
	private String repositoryId;

	public AbstractRepositoryResolvedRequest(IUser user, String repositoryId) {

		this.user = user;
		this.repositoryId = repositoryId;
	}

	@Override
	public IUser getUser() {

		return user;
	}

	@Override
	public String getRepositoryId() {

		return repositoryId;
	}
}