
package com.unnsvc.malmoe.common;

import java.util.List;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IRepositoryManager {

	public IRetrievalResult serveRequest(IResolvedRequest request) throws MalmoeException;

	public IMalmoeRepository getRepository(String repositoryName) throws MalmoeException;

	public List<String> getRepositoryNames();

}
