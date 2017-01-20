
package com.unnsvc.malmoe.common;

import com.unnsvc.malmoe.common.config.IGroupsConfig;
import com.unnsvc.malmoe.common.config.IUsersConfig;

public interface IIdentityManager {

	public IGroupsConfig getGroupsConfig();

	public IUsersConfig getUsersConfig();

	public IUser authenticate(String username, String password);

}
