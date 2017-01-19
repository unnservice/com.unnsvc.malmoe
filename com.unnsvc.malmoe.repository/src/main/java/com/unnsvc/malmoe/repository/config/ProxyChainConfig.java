
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class ProxyChainConfig implements IVisitable {

	private List<IRepositoryConfig> repositoryReferences;

	public ProxyChainConfig(Node node) {

		repositoryReferences = new ArrayList<IRepositoryConfig>();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("repository")) {

				String ref = child.getAttributes().getNamedItem("ref").getNodeValue();
				repositoryReferences.add(new RepositoryConfigRef(ref));
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		repositoryReferences.forEach(repositoryReference -> repositoryReference.visit(visitor.newVisitor()));
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "proxyChain";
	}

}
