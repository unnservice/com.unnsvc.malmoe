
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.config.IUserConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class AnonymousConfig implements IUserConfig {

	private List<IReference> groupReferences;

	public AnonymousConfig(Node node) {

		this.groupReferences = new ArrayList<IReference>();
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

	}

	@Override
	public String serialise(boolean attrs) {

		return "anonymous";
	}

	@Override
	public String getUsername() {

		return "anonymous";
	}

	@Override
	public String getPassword() {

		return null;
	}

	@Override
	public String getSalt() {

		return null;
	}

	@Override
	public List<IReference> getGroupReferences() {

		return groupReferences;
	}

}
