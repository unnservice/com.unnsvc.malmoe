
package com.unnsvc.malmoe.repository.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

import com.unnsvc.malmoe.common.MalmoeConstants;
import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class VirtualRepository implements IRepositoryConfig {

	private String name;
	private AccessConfig accessConfig;
	private List<IResolverConfig> resolverConfigs;

	public VirtualRepository(Node node) throws MalformedURLException, DOMException {

		this.resolverConfigs = new ArrayList<IResolverConfig>();
		this.name = node.getAttributes().getNamedItem("name").getNodeValue();
		for (Node child : Utils.getNodeChildren(node)) {

			if (child.getNamespaceURI().equals(MalmoeConstants.NS_MALMOE_RESOLVER)) {

				URL url = new URL(child.getAttributes().getNamedItem("url").getNodeValue());
				if (child.getLocalName().equals("malmoe")) {

					IResolverConfig resolver = new MalmoeResolverConfig(url);
					resolverConfigs.add(resolver);
				} else if (child.getLocalName().equals("maven")) {

					IResolverConfig resolver = new MavenResolverConfig(url);
					resolverConfigs.add(resolver);
				}
			} else if (child.getLocalName().equals("access")) {

				accessConfig = new AccessConfig(child);
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		accessConfig.visit(visitor.newVisitor());
		resolverConfigs.forEach(resolverConfig -> resolverConfig.visit(visitor.newVisitor()));
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "virtualRepository" + (attrs ? " name=\"" + name + "\"" : "");
	}

}
