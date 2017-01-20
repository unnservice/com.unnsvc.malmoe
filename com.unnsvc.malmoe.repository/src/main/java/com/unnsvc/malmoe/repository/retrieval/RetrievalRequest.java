
package com.unnsvc.malmoe.repository.retrieval;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class RetrievalRequest implements IRetrievalRequest {

	private String repositoryId;
	private ModuleIdentifier identifier;
	private EExecutionType type;

	public RetrievalRequest(String repositoryId, ModuleIdentifier identifier, EExecutionType type) {

		this.repositoryId = repositoryId;
		this.identifier = identifier;
		this.type = type;
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	public EExecutionType getType() {

		return type;
	}

	@Override
	public String getRepositoryName() {

		return repositoryId;
	}

}
