
package com.unnsvc.malmoe.frontend;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class ModelResolvedRequest extends AbstractResolvedRequest {

	public ModelResolvedRequest(String repositoryId, ModuleIdentifier identifier) {

		super(repositoryId, identifier, EExecutionType.MODEL);
	}

}
