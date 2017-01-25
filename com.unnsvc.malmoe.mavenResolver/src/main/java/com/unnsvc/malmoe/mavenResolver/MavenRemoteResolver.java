
package com.unnsvc.malmoe.mavenResolver;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.DependencyNode;
import org.sonatype.aether.repository.RemoteRepository;

import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.repository.retrieval.NotFoundRetrievalResult;
import com.unnsvc.malmoe.resolver.IRemoteResolver;
import com.unnsvc.rhena.common.RhenaConstants;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class MavenRemoteResolver implements IRemoteResolver {

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
	public IRetrievalResult serveRequest(IRetrievalRequest request) throws MalmoeException {

		File groupLocation = new File(resolverLocation, request.getIdentifier().getComponentName().toString().replace(".", File.separator));
		File moduleLocation = new File(groupLocation, request.getIdentifier().getModuleName().toString());
		File versionLocation = new File(moduleLocation, request.getIdentifier().getVersion().toString());
		if (!versionLocation.isDirectory()) {
			versionLocation.mkdirs();
		}

		try {

			StringBuilder sb = new StringBuilder();
			sb.append(request.getIdentifier().getComponentName().toString()).append(":");
			sb.append(request.getIdentifier().getModuleName().toString()).append(":");
			sb.append("jar").append(":");
			sb.append(request.getIdentifier().getVersion().toString());

			MavenDependencyCollector coll = new MavenDependencyCollector(getLocalRepoPath());
			RemoteRepository repo = new RemoteRepository(resolverConfig.getUrl().hashCode() + "", "default", resolverConfig.getUrl().toString());
			DependencyNode node = coll.collectDependencies(sb.toString(), repo);

			if (node.getChildren().isEmpty() || node.getChildren().size() != 1) {
				throw new MalmoeException("Resolved to (" + node.getChildren().size() + "): " + request);
			}

			if (request.getType().equals(EExecutionType.MODEL)) {

				File modelFile = produceModel(request.getIdentifier(), node.getChildren().get(0), versionLocation);
				return new ModelRetrievalResult(modelFile);
			}
		} catch (Exception ex) {
			throw new MalmoeException("Exception while resolving " + request.getIdentifier().toString(), ex);
		}

		return new NotFoundRetrievalResult();
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
	private File produceModel(ModuleIdentifier identifier, DependencyNode node, File versionLocation) throws IOException {

		File modelFile = new File(versionLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
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
		return modelFile;
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
