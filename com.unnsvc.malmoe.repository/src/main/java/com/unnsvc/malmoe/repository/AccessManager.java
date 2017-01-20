
package com.unnsvc.malmoe.repository;

import java.util.Arrays;
import java.util.List;

import com.unnsvc.malmoe.common.IAccess;
import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.config.IAccessConfig;
import com.unnsvc.malmoe.common.config.IGroupConfig;
import com.unnsvc.malmoe.common.config.IPermissionConfig;
import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.exceptions.AccessException;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;

/**
 * Access manager for each access configuration
 * 
 * @author noname
 *
 */
public class AccessManager implements IAccessManager {

	private IAccessConfig accessConfig;
	private IIdentityManager identityManager;

	public AccessManager(IAccessConfig accessConfig, IIdentityManager identityManager) {

		this.accessConfig = accessConfig;
		this.identityManager = identityManager;
	}

	@Override
	public IRetrievalResult withPermissions(IRetrievalRequest request, IAccess<IRetrievalResult> iAccess, String... permissions) throws MalmoeException {

		if (hasPermissions(request.getUser(), permissions)) {

			return iAccess.execute();
		}
		throw new AccessException("Access denied, required permissions: " + Arrays.toString(permissions));
	}

	private boolean hasPermissions(IUser user, String[] permissions) {

		List<String> remainingPermissions = Arrays.asList(permissions);

		for (IReference groupRef : accessConfig) {

			if (user.isInGroup(groupRef.getRef())) {
				for (IGroupConfig groupConfig : identityManager.getGroupsConfig()) {

					if (groupConfig.getGroupName().equals(groupRef.getRef())) {

						for (IPermissionConfig permissionConfig : groupConfig) {

							remainingPermissions.remove(permissionConfig.getId());
						}
					}
				}
			}
		}
		if (remainingPermissions.isEmpty()) {

			return true;
		} else {

			return false;
		}
	}

}
