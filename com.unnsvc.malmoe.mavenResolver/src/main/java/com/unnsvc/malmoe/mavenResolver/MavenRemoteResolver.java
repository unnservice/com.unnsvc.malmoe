
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

		MavenDependencyCollector coll = new MavenDependencyCollector(getLocalRepoPath());
		coll.addRepository(resolverConfig.getUrl());

		String coordinates = identifierToCoordinates(request.getIdentifier(), "jar");

		DependencyNode node = coll.collectDependencies(coordinates);
		produceModelFromMavenCollection(modelFile, request.getIdentifier(), node.getChildren().get(0), moduleLocation);

		DependencyNode artifactNode = coll.resolveDependencies(coordinates);
		produceExecutionFromMavenCollection(moduleLocation, artifactNode.getChildren().get(0));
	}

	private void produceExecutionFromMavenCollection(File moduleLocation, DependencyNode artifactNode) throws IOException, DatatypeConfigurationException {

		File executionTypeLocation = new File(moduleLocation, "main");
		if (!executionTypeLocation.isDirectory()) {
			executionTypeLocation.mkdirs();
		}

		Artifact artifact = artifactNode.getDependency().getArtifact();

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

			writer.write("\t\t<primary name=\"" + artifact.getFile().getName() + "\" sha1=\"" + Utils.generateSha1(artifact.getFile()) + "\" />");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			writer.write("\t</artifact>");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			writer.write("</artifacts>");
			writer.write(RhenaConstants.LINE_SEPARATOR);
		}

		File artifactLocation = new File(executionTypeLocation, artifact.getFile().getName());
		FileUtils.copyFile(artifact.getFile(), artifactLocation);
	}

	private String identifierToCoordinates(ModuleIdentifier identifier, String artifactType) {

		StringBuilder coordinates = new StringBuilder();
		coordinates.append(identifier.getComponentName().toString()).append(":");
		coordinates.append(identifier.getModuleName().toString()).append(":");
		coordinates.append(artifactType).append(":");
		coordinates.append(identifier.getVersion().toString());
		return coordinates.toString();
	}

	private File getLocalRepoPath() {

		String m2Repo = System.getProperty("malmoe.resolver.maven.home");
		if (m2Repo == null) {
			String userHome = System.getProperty("user.home");
			return new File(userHome + File.separator + ".m2" + File.separator + "repository");
		}
		return (new File(m2Repo)).getAbsoluteFile();
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
			writer.write("<module xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:rhena:module\" xmlns:prop=\"urn:rhena:properties\"");
			writer.write("xmlns:dependency=\"urn:rhena:dependency\" xsi:schemaLocation=\"urn:rhena:module http://schema.unnsvc.com/rhena/module.xsd\">");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			writer.write("\t<meta component=\"" + identifier.getComponentName().toString() + "\" version=\"" + identifier.getVersion().toString() + "\" />");
			writer.write(RhenaConstants.LINE_SEPARATOR);

			for (DependencyNode child : node.getChildren()) {

				Artifact artifact = child.getDependency().getArtifact();
				writer.write("\t<dependency:main module=\"" + artifact.getGroupId() + ":" + artifact.getArtifactId() + ":" + artifact.getVersion() + "\" />");
				writer.write(RhenaConstants.LINE_SEPARATOR);
			}

			writer.write("</module>");
			writer.write(RhenaConstants.LINE_SEPARATOR);
		}
	}

	private String toNotation(ModuleIdentifier identifier) {

		StringBuilder sb = new StringBuilder();
		sb.append(identifier.getComponentName().toString()).append(":");
		sb.append(identifier.getModuleName().toString()).append(":");
		sb.append("jar").append(":");
		sb.append(identifier.getVersion().toString());
		return sb.toString();
	}

}
