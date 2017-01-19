
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class PermissionConfig implements IVisitable {

	private String id;

	public PermissionConfig(String id) {

		this.id = id;
	}

	public String getId() {

		return id;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String serialise(boolean attr) {

		return "permission" + (attr ? " id=\"" + id + "\"" : "");
	}
}
