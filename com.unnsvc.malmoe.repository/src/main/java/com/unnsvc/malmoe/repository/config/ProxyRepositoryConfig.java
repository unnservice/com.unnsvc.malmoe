
package com.unnsvc.malmoe.repository.config;

import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IAccessConfig;
import com.unnsvc.malmoe.common.config.IProxyChainConfig;
import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class ProxyRepositoryConfig implements IRepositoryConfig {

	private String name;
	private IAccessConfig accessConfig;
	private ProxyChainConfig proxyChainConfig;

	public ProxyRepositoryConfig(Node node) {

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

		return "proxyRepository" + (attrs ? " name=\"" + name + "\"" : "");
	}

	@Override
	public String getRepositoryName() {

		return name;
	}

	@Override
	public IAccessConfig getAccessConfig() {

		return accessConfig;
	}

	public IProxyChainConfig getProxyChainConfig() {

		return proxyChainConfig;
	}

}
