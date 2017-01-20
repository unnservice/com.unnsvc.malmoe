
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.config.IGroupsConfig;
import com.unnsvc.malmoe.common.config.IIdentityConfig;
import com.unnsvc.malmoe.common.config.IUsersConfig;

public class IdentityManager implements IIdentityManager {

	private IGroupsConfig groupsConfig;
	private IUsersConfig usersConfig;

	public IdentityManager(IIdentityConfig identityConfig) {

		this.groupsConfig = identityConfig.getGroupConfig();
		this.usersConfig = identityConfig.getUsersConfig();
	}

	@Override
	public IGroupsConfig getGroupsConfig() {

		return groupsConfig;
	}

	@Override
	public IUsersConfig getUsersConfig() {

		return usersConfig;
	}

	@Override
	public IUser authenticate(String username, String password) {

		throw new UnsupportedOperationException("Not implemented");
	}
}
