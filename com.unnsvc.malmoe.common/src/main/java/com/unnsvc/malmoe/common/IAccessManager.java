
package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.exceptions.MalmoeException;

public interface IAccessManager {

	public IRetrievalResult withPermissions(IRetrievalRequest request, IAccess<IRetrievalResult> iAccess, String... permission) throws MalmoeException;

}
