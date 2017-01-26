
package com.unnsvc.malmoe.repository.identity;

import java.util.List;

import com.unnsvc.malmoe.common.IUser;
import com.unnsvc.malmoe.common.config.IReference;

public class User implements IUser {

	private String username;
	private List<IReference> groupRefs;

	public User(String username, List<IReference> groupReferences) {

		this.username = username;
		this.groupRefs = groupReferences;
	}

	@Override
	public String getUsername() {

		return username;
	}

	@Override
	public List<IReference> getGroupRefs() {

		return groupRefs;
	}

	@Override
	public boolean isInGroup(String groupName) {

		for (IReference ref : groupRefs) {
			if (ref.getRef().equals(groupName)) {
				return true;
			}
		}
		return false;
	}
}
