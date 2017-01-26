
package com.unnsvc.malmoe.repository.retrieval;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.resolver.ERequestType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RetrievalRequest implements IRetrievalRequest {

	private IUser user;
	private String repositoryId;
	private ModuleIdentifier identifier;
	private ERequestType type;

	public RetrievalRequest(IUser user, String repositoryId, ModuleIdentifier identifier, ERequestType type) {

		this.user = user;
		this.repositoryId = repositoryId;
		this.identifier = identifier;
		this.type = type;
	}

	@Override
	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	@Override
	public ERequestType getType() {

		return type;
	}

	@Override
	public String getRepositoryName() {

		return repositoryId;
	}

	@Override
	public IUser getUser() {

		return user;
	}
}
