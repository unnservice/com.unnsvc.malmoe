
package com.unnsvc.malmoe.repository.config;

import com.unnsvc.malmoe.common.config.IPermissionConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class PermissionConfig implements IPermissionConfig {

	private String id;

	public PermissionConfig(String id) {

		this.id = id;
	}

	@Override
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
