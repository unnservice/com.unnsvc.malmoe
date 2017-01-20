
package com.unnsvc.malmoe.repository.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IProxyChainConfig;
import com.unnsvc.malmoe.common.config.IReference;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class ProxyChainConfig implements IProxyChainConfig {

	private List<IReference> repositoryReferences;

	public ProxyChainConfig(Node node) {

		repositoryReferences = new ArrayList<IReference>();
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

	@Override
	public Iterator<IReference> iterator() {

		return repositoryReferences.iterator();
	}

}
