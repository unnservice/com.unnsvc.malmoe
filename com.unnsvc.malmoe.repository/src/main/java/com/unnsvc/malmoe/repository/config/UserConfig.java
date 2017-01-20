
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class UserConfig implements IVisitable {

	private String username;
	private String password;
	private List<IReference> groupReferences;

	public UserConfig(Node node) {

		this.groupReferences = new ArrayList<IReference>();
		this.username = node.getAttributes().getNamedItem("username").getNodeValue();
		this.password = node.getAttributes().getNamedItem("password").getNodeName();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("group")) {
				String ref = child.getAttributes().getNamedItem("ref").getNodeValue();
				IReference groupRef = new GroupConfigRef(ref);
				this.groupReferences.add(groupRef);
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.visitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "user" + (attrs ? " username=\"" + username + "\" password=\"" + password + "\"" : "");
	}

}
