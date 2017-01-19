
package com.unnsvc.malmoe.repository.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.MalmoeConstants;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.malmoe.common.visitors.ResolutionVisitor;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.exceptions.RhenaException;

public class MalmoeConfigurationParser implements IMalmoeConfiguration {

	private IdentityConfig identityConfig;
	private RepositoriesConfig repositoryConfig;

	public MalmoeConfigurationParser(File workspaceDirectory) throws RhenaException {

		File repositoryConfigFile = new File(workspaceDirectory, MalmoeConstants.DEFAULT_REPOSITORY_FILENAME);
		try {
			parse(repositoryConfigFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {

			throw new RhenaException(e.getMessage(), e);
		}
		visit(new ResolutionVisitor());
	}

	private void parse(File repositoryConfigFile) throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder builder = fact.newDocumentBuilder();
		Document document = builder.parse(repositoryConfigFile);

		Node repositoryNode = Utils.getChildNode(document, "repository");
		for (Node node : Utils.getNodeChildren(repositoryNode)) {

			if (node.getLocalName().equals("identity")) {
				identityConfig = new IdentityConfig(node);
			} else if (node.getLocalName().equals("repository")) {
				repositoryConfig = new RepositoriesConfig(node);
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		identityConfig.visit(visitor);
		repositoryConfig.visit(visitor);
		visitor.endVisitable(this);
	}
}
