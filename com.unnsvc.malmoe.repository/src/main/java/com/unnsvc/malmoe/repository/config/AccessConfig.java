
package com.unnsvc.malmoe.repository.config;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IGroupConfig;
import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class AccessConfig implements IVisitable {

	private Set<IGroupConfig> groupConfigs;

	public AccessConfig(Node node) {

		this.groupConfigs = new HashSet<IGroupConfig>();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("group")) {

				String groupName = child.getAttributes().getNamedItem("ref").getNodeValue();
				groupConfigs.add(new GroupConfigRef(groupName));
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

		return "access";
	}

}
