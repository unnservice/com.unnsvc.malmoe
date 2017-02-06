
package com.unnsvc.malmoe.mavenResolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.DependencyNode;

import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.requests.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.requests.ModelRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.resolver.IRemoteResolver;
import com.unnsvc.malmoe.common.retrieval.ArtifactRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.NotFoundRetrievalResult;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.Utils;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class MavenRemoteResolver implements IRemoteResolver {

	public static String DEFAULT_MAVEN_LOCALREPO_PROP = "malmoe.resolver.maven.home";
	private Logger log = LoggerFactory.getLogger(getClass());
	private IResolverConfig resolverConfig;
	private File resolverLocation;

	public MavenRemoteResolver(IResolverConfig resolverConfig, File resolverLocation) {

		this.resolverConfig = resolverConfig;
		this.resolverLocation = resolverLocation;
	}

	/**
	 * This will first attempt to resolve from the storage, if it doesn't exist
	 * then resolve from remote
	 */
	@Override
	public IRetrievalResult serveRequest(IResolvedArtifactRequest request) throws MalmoeException {

		File groupLocation = new File(resolverLocation, request.getIdentifier().getComponentName().toString().replace(".", File.separator));
		File moduleNameLocation = new File(groupLocation, request.getIdentifier().getModuleName().toString());
		File moduleLocation = new File(moduleNameLocation, request.getIdentifier().getVersion().toString());
		if (!moduleLocation.isDirectory()) {
			moduleLocation.mkdirs();
		}

		try {
			File modelFile = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
			if (!modelFile.exists()) {
				try {
					adaptMavenArtifact(request, modelFile, moduleLocation);
				} catch (Exception ex) {
					log.debug(ex.getMessage());
					return new NotFoundRetrievalResult(request);
				}
			}

			if (request instanceof ModelRepositoryResolvedRequest) {

				return new ModelRetrievalResult(modelFile);
			} else if (request instanceof ArtifactRepositoryResolvedRequest) {

				ArtifactRepositoryResolvedRequest artifactRequest = (ArtifactRepositoryResolvedRequest) request;
				File executionTypeLocation = new File(moduleLocation, artifactRequest.getExecutionType().literal());
				File artifactFile = new File(executionTypeLocation, artifactRequest.getArtifactName());
				if (artifactFile.exists()) {
					return new ArtifactRetrievalResult(artifactFile);
				}
			}

		} catch (Exception ex) {
			throw new MalmoeException("Exception while resolving " + request.getIdentifier().toString(), ex);
		}

		return new NotFoundRetrievalResult(request);
	}

	private void adaptMavenArtifact(IResolvedArtifactRequest request, File modelFile, File moduleLocation) throws Exception {

		MavenDependencyCollector coll = new MavenDependencyCollector();
		coll.addRepository(resolverConfig.getUrl());

		String groupId = request.getIdentifier().getComponentName().toString();
		String artifactId = request.getIdentifier().getModuleName().toString();
		String version = request.getIdentifier().getVersion().toString();

		coll.addDependency(groupId, artifactId, "", "jar", version);
		coll.addDependency(groupId, artifactId, "sources", "jar", version);
		coll.addDependency(groupId, artifactId, "javadoc", "jar", version);

		DependencyNode collectionRoot = coll.collectDependencies();
		produceModelFromMavenCollection(modelFile, request.getIdentifier(), collectionRoot, moduleLocation);

		DependencyNode resolutionNode = coll.resolveDependencies();
		produceExecutionFromMavenCollection(moduleLocation, resolutionNode);
	}

	private void produceExecutionFromMavenCollection(File moduleLocation, DependencyNode artifactNode) throws IOException, DatatypeConfigurationException {

		File executionTypeLocation = new File(moduleLocation, "main");
		if (!executionTypeLocation.isDirectory()) {
			executionTypeLocation.mkdirs();
		}

		File executionFile = new File(executionTypeLocation, RhenaConstants.ARTIFACTS_DESCRIPTOR_FILENAME);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(executionFile))) {
			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			writer.write(RhenaConstants.LINE_SEPARATOR);
			writer.write("<artifacts xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:rhena:artifacts\">");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone("EST"));
			String convertedDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc).toXMLFormat();

			writer.write("\t<meta date=\"" + convertedDate + "\" />");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			writer.write("\t<artifact classifier=\"default\">");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			for (DependencyNode childNode : artifactNode.getChildren()) {
				Artifact artifact = childNode.getDependency().getArtifact();
				// Copy in place
				File artifactLocation = new File(executionTypeLocation, artifact.getFile().getName());
				FileUtils.copyFile(artifact.getFile(), artifactLocation);

				if (artifact.getClassifier().isEmpty()) {
					writer.write("\t\t<primary name=\"" + artifact.getFile().getName() + "\" sha1=\"" + Utils.generateSha1(artifact.getFile()) + "\" />");
				} else if (artifact.getClassifier().equals("sources")) {
					writer.write("\t\t<sources name=\"" + artifact.getFile().getName() + "\" sha1=\"" + Utils.generateSha1(artifact.getFile()) + "\" />");
				} else if (artifact.getClassifier().equals("javadoc")) {
					writer.write("\t\t<javadoc name=\"" + artifact.getFile().getName() + "\" sha1=\"" + Utils.generateSha1(artifact.getFile()) + "\" />");
				}
				writer.write(RhenaConstants.LINE_SEPARATOR);
			}

			writer.write("\t</artifact>");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			writer.write("</artifacts>");
			writer.write(RhenaConstants.LINE_SEPARATOR);
		}
	}

	/**
	 * @param identifier
	 * @param set
	 * @TODO Need to build models too
	 * @param dependenciesGraph
	 * @throws IOException
	 */
	private void produceModelFromMavenCollection(File modelFile, ModuleIdentifier identifier, DependencyNode node, File versionLocation) throws IOException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(modelFile))) {

			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			writer.write(RhenaConstants.LINE_SEPARATOR);
			writer.write("<module xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:rhena:module\" xmlns:prop=\"urn:rhena:properties\" ");
			writer.write("xmlns:dependency=\"urn:rhena:dependency\" xsi:schemaLocation=\"urn:rhena:module http://schema.unnsvc.com/rhena/module.xsd\">");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			writer.write("\t<meta component=\"" + identifier.getComponentName().toString() + "\" version=\"" + identifier.getVersion().toString() + "\" />");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			for (DependencyNode child : node.getChildren()) {

				/**
				 * Only care about the dependencies of the null classifier
				 * (default)
				 */
				if (child.getDependency().getArtifact().getClassifier().isEmpty()) {

					for (DependencyNode dep : child.getChildren()) {
						Artifact artifact = dep.getDependency().getArtifact();
						writer.write("\t<dependency:main module=\"" + artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + "\" />");
						writer.write(RhenaConstants.LINE_SEPARATOR);
					}
					break;
				} else {
					System.err.println("Classifier was: " + child.getDependency().getArtifact().getClassifier());
				}
			}

			writer.write("</module>");
			writer.write(RhenaConstants.LINE_SEPARATOR);
		}
	}
}
