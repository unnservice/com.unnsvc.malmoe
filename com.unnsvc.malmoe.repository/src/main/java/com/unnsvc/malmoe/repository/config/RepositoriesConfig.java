
package com.unnsvc.malmoe.repository.config;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.unnsvc.malmoe.common.config.IRepositoryConfig;
import com.unnsvc.malmoe.common.visitors.IVisitable;
import com.unnsvc.malmoe.common.visitors.IVisitor;

public class RepositoriesConfig implements IVisitable {

	private Set<IRepositoryConfig> repositoryConfigs;

	public RepositoriesConfig(Node node) throws MalformedURLException, DOMException {

		this.repositoryConfigs = new HashSet<IRepositoryConfig>();

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {

			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {

				if (child.getLocalName().equals("proxyRepository")) {

					IRepositoryConfig repo = new ProxyRepository(child);
					repositoryConfigs.add(repo);
				} else if (child.getLocalName().equals("virtualRepository")) {

					IRepositoryConfig repo = new VirtualRepository(child);
					repositoryConfigs.add(repo);
				}
			}
		}
	}

	public Set<IRepositoryConfig> getRepositoryConfigs() {

		return repositoryConfigs;
	}

	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		repositoryConfigs.forEach(repositoryConfig -> repositoryConfig.visit(visitor));
		visitor.endVisitable(this);
	}
}
