
package com.unnsvc.malmoe.frontend.resolved;

import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModelRepositoryResolvedRequest extends AbstractRepositoryResolvedRequest {

	private ModuleIdentifier moduleIdentifier;

	public ModelRepositoryResolvedRequest(String repositoryId, ModuleIdentifier moduleIdentifier) {

		super(repositoryId);
		this.moduleIdentifier = moduleIdentifier;
	}

	public ModuleIdentifier getModuleIdentifier() {

		return moduleIdentifier;
	}
}
