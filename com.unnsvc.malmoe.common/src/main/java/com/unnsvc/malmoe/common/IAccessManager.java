
package com.unnsvc.malmoe.common;

public interface IAccessManager {

	public IRetrievalResult withPermissions(IAccess<IRetrievalResult> iAccess, String... permission);

}
