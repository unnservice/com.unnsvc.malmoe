
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IAccess;
import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IAccessConfig;

/**
 * Access manager for each access configuration
 * 
 * @author noname
 *
 */
public class AccessManager implements IAccessManager {

	public AccessManager(IAccessConfig accessConfig, IIdentityManager identityManager) {

	}

	@Override
	public IRetrievalResult withPermissions(IAccess<IRetrievalResult> iAccess, String... permission) {

		throw new UnsupportedOperationException("Not implemented");
	}

}
