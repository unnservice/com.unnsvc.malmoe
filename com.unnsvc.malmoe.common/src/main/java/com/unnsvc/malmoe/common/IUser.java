
package com.unnsvc.malmoe.common;

import java.util.List;

import com.unnsvc.malmoe.common.config.IReference;

public interface IUser {

	public String getUsername();

	public List<IReference> getGroupRefs();

	public boolean isInGroup(String groupName);
}
