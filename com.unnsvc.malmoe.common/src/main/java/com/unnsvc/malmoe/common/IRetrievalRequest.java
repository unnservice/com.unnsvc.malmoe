
package com.unnsvc.malmoe.common;

import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRetrievalRequest {

	public ModuleIdentifier getIdentifier();

	public EExecutionType getType();

	public String getRepositoryName();

	public IUser getUser();

}
