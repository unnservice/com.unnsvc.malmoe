
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.config.IGroupConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class GroupConfigRef implements IGroupConfig {

	private String ref;

	public GroupConfigRef(String ref) {

		this.ref = ref;
	}

	public String getGroupName() {

		return ref;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "group" + (attrs ? " ref=\"" + ref + "\"" : "");
	}
}
