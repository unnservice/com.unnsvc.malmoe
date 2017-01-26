
package com.unnsvc.malmoe.repository.retrieval;

import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.resolver.ERequestType;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ArtifactRetrievalRequest extends RetrievalRequest {

	private EExecutionType executionType;
	private String artifactName;

	public ArtifactRetrievalRequest(IUser user, String repositoryId, ModuleIdentifier identifier, EExecutionType executionType, String artifactName) {

		super(user, repositoryId, identifier, ERequestType.ARTIFACT);

		this.executionType = executionType;
		this.artifactName = artifactName;
	}

	public EExecutionType getExecutionType() {

		return executionType;
	}

	public String getArtifactName() {

		return artifactName;
	}
}
