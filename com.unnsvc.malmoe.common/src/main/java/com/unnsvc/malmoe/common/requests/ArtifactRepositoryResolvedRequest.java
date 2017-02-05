
package com.unnsvc.malmoe.common.requests;

import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ArtifactRepositoryResolvedRequest extends AbstractRepositoryResolvedRequest implements IResolvedArtifactRequest {

	private ModuleIdentifier moduleIdentifier;
	private EExecutionType executionType;
	private String artifactName;

	public ArtifactRepositoryResolvedRequest(IUser user, String repositoryId, ModuleIdentifier moduleIdentifier, EExecutionType executionType, String artifactName) {

		super(user, repositoryId);
		this.moduleIdentifier = moduleIdentifier;
		this.executionType = executionType;
		this.artifactName = artifactName;
	}

	public ModuleIdentifier getIdentifier() {

		return moduleIdentifier;
	}

	public EExecutionType getExecutionType() {

		return executionType;
	}

	public String getArtifactName() {

		return artifactName;
	}
}
