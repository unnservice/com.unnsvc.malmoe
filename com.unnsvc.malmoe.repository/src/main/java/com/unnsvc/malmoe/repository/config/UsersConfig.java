
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class UsersConfig implements IVisitable {

	private List<UserConfig> userConfigs;

	public UsersConfig(Node node) {

		this.userConfigs = new ArrayList<UserConfig>();

		for (Node child : Utils.getNodeChildren(node)) {

			UserConfig userConfig = new UserConfig(child);
			userConfigs.add(userConfig);
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		userConfigs.forEach(userConfig -> userConfig.visit(visitor.newVisitor()));
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "users";
	}

}
