
package com.unnsvc.malmoe.repository.retrieval;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RetrievalRequest implements IRetrievalRequest {

	private IUser user;
	private String repositoryId;
	private ModuleIdentifier identifier;
	private EExecutionType type;

	public RetrievalRequest(IUser user, String repositoryId, ModuleIdentifier identifier, EExecutionType type) {

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
	public EExecutionType getType() {

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
