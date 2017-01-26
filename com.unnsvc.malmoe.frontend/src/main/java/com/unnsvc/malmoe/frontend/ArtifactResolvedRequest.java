
package com.unnsvc.malmoe.frontend;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ArtifactResolvedRequest extends AbstractResolvedRequest {

	private String artifactName;

	public ArtifactResolvedRequest(String repositoryId, ModuleIdentifier identifier, EExecutionType executionType, String artifactName) {

		super(repositoryId, identifier, executionType);
		this.artifactName = artifactName;
	}

	public String getArtifactName() {

		return artifactName;
	}

}
