
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class PermissionConfig implements IVisitable {

	private String access;

	public PermissionConfig(String access) {

		this.access = access;
	}

	public String getAccess() {

		return access;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}
}
