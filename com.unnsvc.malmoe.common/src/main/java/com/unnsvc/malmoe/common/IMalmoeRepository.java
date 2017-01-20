package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IMalmoeRepository {

	public IRetrievalResult retrieveModule(IRetrievalRequest request) throws MalmoeException;

}
