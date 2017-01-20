
package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IMalmoeRepository {

	public static final String ACCESS_REPOSITORY_READ = "repository.read";
	public static final String ACCESS_REPOSITORY_WRITE = "repository.write";

	public IRetrievalResult retrieveModule(IRetrievalRequest request) throws MalmoeException;

}
