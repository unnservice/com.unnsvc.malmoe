
package com.unnsvc.malmoe.repository;

import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.config.IGroupsConfig;
import com.unnsvc.malmoe.common.config.IIdentityConfig;
import com.unnsvc.malmoe.common.config.IUserConfig;
import com.unnsvc.malmoe.common.config.IUsersConfig;
import com.unnsvc.malmoe.common.exceptions.AccessException;
import com.unnsvc.malmoe.repository.identity.User;

public class IdentityManager implements IIdentityManager {

	private PasswordOperations passwordOperations;
	private IGroupsConfig groupsConfig;
	private IUsersConfig usersConfig;

	public IdentityManager(IIdentityConfig identityConfig) {

		this.passwordOperations = new PasswordOperations(256, 100);
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
	public IUser authenticate(String username, String password) throws AccessException {

		for (IUserConfig userConfig : usersConfig) {

			if (userConfig.getUsername().equals(username)) {

				String passwordHash = userConfig.getPassword();
				String salt = userConfig.getSalt();
				if (passwordOperations.verifyPassword(password, salt, passwordHash)) {

					return new User(userConfig);
				} else {
					throw new AccessException("Authentication failed");
				}
			}
		}
		throw new AccessException("No such user: " + username);
	}
}
