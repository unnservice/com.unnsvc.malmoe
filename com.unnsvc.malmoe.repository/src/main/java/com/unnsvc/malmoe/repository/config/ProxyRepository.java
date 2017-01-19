
package com.unnsvc.malmoe.repository.config;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class ProxyRepository implements IRepositoryConfig {

	private String name;
	private AccessConfig accessConfig;
	private ProxyChainConfig proxyChainConfig;

	public ProxyRepository(Node node) {

		this.name = node.getAttributes().getNamedItem("name").getNodeValue();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("access")) {

				accessConfig = new AccessConfig(child);
			} else if (child.getLocalName().equals("proxyChain")) {

				proxyChainConfig = new ProxyChainConfig(child);
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		accessConfig.visit(visitor.newVisitor());
		proxyChainConfig.visit(visitor.newVisitor());
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "proxy" + (attrs ? " name=\"" + name + "\"" : "");
	}

}
