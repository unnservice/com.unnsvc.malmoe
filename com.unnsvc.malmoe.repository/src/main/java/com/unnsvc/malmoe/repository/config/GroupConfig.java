
package com.unnsvc.malmoe.repository.config;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IGroupConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class GroupConfig implements IGroupConfig {

	private String nodeName;

	private Set<PermissionConfig> permissionConfigs;

	public GroupConfig(Node node) {

		this.permissionConfigs = new HashSet<PermissionConfig>();
		nodeName = node.getAttributes().getNamedItem("name").getNodeValue();
		for (Node child : Utils.getNodeChildren(node)) {
			if (child.getLocalName().equals("permission")) {

				String id = child.getAttributes().getNamedItem("id").getNodeValue();
				permissionConfigs.add(new PermissionConfig(id));
			}
		}
	}

	public GroupConfig() {

	}

	public Set<PermissionConfig> getPermissionConfigs() {

		return permissionConfigs;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		permissionConfigs.forEach(permissionConfig -> permissionConfig.visit(visitor.newVisitor()));
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attr) {

		return "group" + (attr ? " name=\"" + nodeName + "\"" : "");
	}

}
