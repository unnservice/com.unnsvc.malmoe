
package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IAccess<T extends IRetrievalResult> {

	public T execute() throws MalmoeException;
}
