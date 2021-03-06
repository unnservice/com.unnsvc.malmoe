
package com.unnsvc.malmoe.common.requests;

import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModelRepositoryResolvedRequest extends AbstractRepositoryResolvedRequest implements IResolvedArtifactRequest {

	private ModuleIdentifier moduleIdentifier;

	public ModelRepositoryResolvedRequest(IUser user, String repositoryId, ModuleIdentifier moduleIdentifier) {

		super(user, repositoryId);
		this.moduleIdentifier = moduleIdentifier;
	}

	public ModuleIdentifier getIdentifier() {

		return moduleIdentifier;
	}

	@Override
	public String toString() {

		return "ModelRepositoryResolvedRequest [moduleIdentifier=" + moduleIdentifier + ", toString()=" + super.toString() + "]";
	}
}
