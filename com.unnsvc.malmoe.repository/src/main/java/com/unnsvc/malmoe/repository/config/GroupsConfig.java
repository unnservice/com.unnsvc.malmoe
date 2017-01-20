
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IGroupConfig;
import com.unnsvc.malmoe.common.config.IGroupsConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class GroupsConfig implements IGroupsConfig {

	private List<IGroupConfig> groupConfigs;

	public GroupsConfig(Node node) {

		groupConfigs = new ArrayList<IGroupConfig>();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("group")) {
				GroupConfig groupConfig = new GroupConfig(child);
				groupConfigs.add(groupConfig);
			}
		}
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

	@Override
	public Iterator<IGroupConfig> iterator() {

		return groupConfigs.iterator();
	}
}
