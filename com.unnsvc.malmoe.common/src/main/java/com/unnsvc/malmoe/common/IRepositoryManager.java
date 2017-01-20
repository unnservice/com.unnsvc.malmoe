package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IRepositoryManager {

	public IRetrievalResult retrieveModule(IRetrievalRequest request) throws MalmoeException;

	public IMalmoeRepository getRepository(String repositoryName) throws MalmoeException;

}
