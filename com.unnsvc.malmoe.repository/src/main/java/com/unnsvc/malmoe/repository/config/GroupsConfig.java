
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class GroupsConfig implements IVisitable {

	private List<GroupConfig> groupConfigs;

	public GroupsConfig(Node node) {

		groupConfigs = new ArrayList<GroupConfig>();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("group")) {
				GroupConfig groupConfig = new GroupConfig(child);
				groupConfigs.add(groupConfig);
			}
		}
	}

	public List<GroupConfig> getGroupConfigs() {

		return groupConfigs;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		groupConfigs.forEach(groupConfig -> groupConfig.visit(visitor.newVisitor()));
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "groups";
	}
}
