
package com.unnsvc.malmoe.frontend.resolved;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ArtifactRepositoryResolvedRequest extends AbstractRepositoryResolvedRequest {

	private ModuleIdentifier moduleIdentifier;
	private EExecutionType executionType;
	private String artifactName;

	public ArtifactRepositoryResolvedRequest(String repositoryId, ModuleIdentifier moduleIdentifier, EExecutionType executionType, String artifactName) {

		super(repositoryId);
		this.moduleIdentifier = moduleIdentifier;
		this.executionType = executionType;
		this.artifactName = artifactName;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}

	public EExecutionType getExecutionType() {

		return executionType;
	}

	public String getArtifactName() {

		return artifactName;
	}
}
