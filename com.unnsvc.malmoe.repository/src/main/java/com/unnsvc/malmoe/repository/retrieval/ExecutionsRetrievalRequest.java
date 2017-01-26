
package com.unnsvc.malmoe.repository.retrieval;

import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.resolver.ERequestType;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ExecutionsRetrievalRequest extends RetrievalRequest {

	private EExecutionType executionType;

	public ExecutionsRetrievalRequest(IUser user, String repositoryId, ModuleIdentifier identifier, EExecutionType executionType) {

		super(user, repositoryId, identifier, ERequestType.EXECUTIONS);
		this.executionType = executionType;
	}

	public EExecutionType getExecutionType() {

		return executionType;
	}

}
