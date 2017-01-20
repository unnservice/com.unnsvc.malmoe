
package com.unnsvc.malmoe.repository.config;

import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.config.IAccessConfig;
import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class VirtualRepositoryConfig implements IRepositoryConfig {

	private String name;
	private AccessConfig accessConfig;
	private IResolverConfig resolverConfig;

	public VirtualRepositoryConfig(Node node) throws MalformedURLException, DOMException {

		this.name = node.getAttributes().getNamedItem("name").getNodeValue();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getLocalName().equals("resolver")) {

				URL url = new URL(child.getAttributes().getNamedItem("url").getNodeValue());
				String resolverName = child.getAttributes().getNamedItem("name").getNodeValue();
				
				resolverConfig = new ResolverConfig(resolverName, url);
			} else if (child.getLocalName().equals("access")) {

				accessConfig = new AccessConfig(child);
			}
		}
	}

	@Override
	public String getRepositoryName() {

		return name;
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		accessConfig.visit(visitor.newVisitor());
		resolverConfig.visit(visitor.newVisitor());
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "virtualRepository" + (attrs ? " name=\"" + name + "\"" : "");
	}

	public IResolverConfig getResolverConfig() {

		return resolverConfig;
	}

	@Override
	public IAccessConfig getAccessConfig() {

		return accessConfig;
	}
}
