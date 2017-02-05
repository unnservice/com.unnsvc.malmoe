
package com.unnsvc.malmoe.repository.config;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.unnsvc.malmoe.common.IMalmoeConfiguration;
import com.unnsvc.malmoe.common.MalmoeConstants;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.visitors.IVisitor;
import com.unnsvc.rhena.common.Utils;

public class MalmoeConfigurationParser implements IMalmoeConfiguration {

	private IdentityConfig identityConfig;
	private RepositoriesConfig repositoryConfig;

	public MalmoeConfigurationParser(File workspaceDirectory) throws MalmoeException {

		File confiDirectory = new File(workspaceDirectory, "conf");
		File repositoryConfigFile = new File(confiDirectory, MalmoeConstants.DEFAULT_REPOSITORY_FILENAME);
		try {
			parse(repositoryConfigFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {

			throw new MalmoeException(e.getMessage(), e);
		}
	}

	private void parse(File repositoryConfigFile) throws ParserConfigurationException, SAXException, IOException, MalmoeException {

		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		fact.setNamespaceAware(true);
		DocumentBuilder builder = fact.newDocumentBuilder();
		Document document = builder.parse(repositoryConfigFile);

		// validate
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(getClass().getClassLoader().getResource("META-INF/schema/repository.xsd"));

		try {
			Validator validator = schema.newValidator();
			validator.validate(new DOMSource(document));
		} catch (Exception ex) {
			throw new MalmoeException("Schema validation error for: " + repositoryConfigFile, ex);
		}

		Node repositoryNode = Utils.getChildNode(document, "repository");
		for (Node node : Utils.getNodeChildren(repositoryNode)) {

			if (node.getLocalName().equals("identity")) {
				identityConfig = new IdentityConfig(node);
			} else if (node.getLocalName().equals("repositories")) {
				repositoryConfig = new RepositoriesConfig(node);
			}
		}
	}

	@Override
	public void visit(IVisitor visitor) {

		visitor.startVisitable(this);
		identityConfig.visit(visitor.newVisitor());
		repositoryConfig.visit(visitor.newVisitor());
		visitor.endVisitable(this);
	}

	@Override
	public String serialise(boolean attrs) {

		return "repository" + (attrs ? " xmlns=\"" + MalmoeConstants.NS_MALMOE_REPOSITORY + "\"" : "");
	}

	@Override
	public IdentityConfig getIdentityConfig() {

		return identityConfig;
	}

	@Override
	public RepositoriesConfig getRepositoriesConfig() {

		return repositoryConfig;
	}
}
