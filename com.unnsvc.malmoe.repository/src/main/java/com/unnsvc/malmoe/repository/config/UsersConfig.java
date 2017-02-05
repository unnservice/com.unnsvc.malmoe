
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IUserConfig;
import com.unnsvc.malmoe.common.config.IUsersConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class UsersConfig implements IUsersConfig {

	private List<IUserConfig> userConfigs;

	public UsersConfig(Node node) {

		this.userConfigs = new ArrayList<IUserConfig>();

		for (Node child : Utils.getNodeChildren(node)) {
			if (child.getLocalName().equals("anonymous")) {
				AnonymousConfig userConfig = new AnonymousConfig(child);
				userConfigs.add(userConfig);
			} else {
				UserConfig userConfig = new UserConfig(child);
				userConfigs.add(userConfig);
			}
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

	@Override
	public Iterator<IUserConfig> iterator() {

		return userConfigs.iterator();
	}

}
