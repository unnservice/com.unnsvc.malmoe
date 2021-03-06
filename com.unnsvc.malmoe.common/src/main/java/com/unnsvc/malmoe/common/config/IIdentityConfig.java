
package com.unnsvc.malmoe.common.config;

import com.unnsvc.malmoe.common.visitors.IVisitable;

public interface IIdentityConfig extends IVisitable {

	public IGroupsConfig getGroupConfig();

	public IUsersConfig getUsersConfig();

}
