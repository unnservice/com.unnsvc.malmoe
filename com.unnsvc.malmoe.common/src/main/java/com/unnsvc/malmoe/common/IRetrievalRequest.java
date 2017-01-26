
package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.resolver.ERequestType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public interface IRetrievalRequest {
	
	public ModuleIdentifier getIdentifier();

	public ERequestType getType();

	public String getRepositoryName();

	public IUser getUser();

}
