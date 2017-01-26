
package com.unnsvc.malmoe.frontend;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class AbstractResolvedRequest implements IResolvedRequest {

	private String repositoryId;
	private ModuleIdentifier identifier;
	private EExecutionType executionType;

	public AbstractResolvedRequest(String repositoryId, ModuleIdentifier identifier, EExecutionType executionType) {

		this.repositoryId = repositoryId;
		this.identifier = identifier;
		this.executionType = executionType;
	}

	public String getRepositoryId() {

		return repositoryId;
	}

	public ModuleIdentifier getIdentifier() {

		return identifier;
	}

	public EExecutionType getExecutionType() {

		return executionType;
	}
}
