
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.config.IGroupConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class GroupConfigRef implements IGroupConfig {

	private String groupName;

	public GroupConfigRef(String groupName) {

		this.groupName = groupName;
	}

	public String getGroupName() {

		return groupName;
	}

	@Override
	public void visit(IVisitor visitor) {

		throw new UnsupportedOperationException("Not visitable");
	}
}
