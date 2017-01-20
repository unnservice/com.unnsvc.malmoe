
package com.unnsvc.malmoe.repository.config;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IIdentityConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class IdentityConfig implements IIdentityConfig {

	private GroupsConfig groupConfig;
	private UsersConfig usersConfig;

	public IdentityConfig(Node node) {

		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("groups")) {

				groupConfig = new GroupsConfig(child);
			} else if (child.getLocalName().equals("users")) {

				usersConfig = new UsersConfig(child);
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		groupConfig.visit(visitor.newVisitor());
		usersConfig.visit(visitor.newVisitor());
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "identity";
	}
}
