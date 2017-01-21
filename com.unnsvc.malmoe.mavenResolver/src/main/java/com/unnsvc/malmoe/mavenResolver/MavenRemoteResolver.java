
package com.unnsvc.malmoe.mavenResolver;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.sonatype.aether.graph.Dependency;

import com.tobedevoured.naether.api.Naether;
import com.tobedevoured.naether.impl.NaetherImpl;
import com.unnsvc.malmoe.common.IRetrievalRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.config.IResolverConfig;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.repository.retrieval.NotFoundRetrievalResult;
import com.unnsvc.malmoe.resolver.IRemoteResolver;
import com.unnsvc.rhena.common.execution.EExecutionType;
import com.unnsvc.rhena.common.identity.ModuleIdentifier;

public class MavenRemoteResolver implements IRemoteResolver {

	private IResolverConfig resolverConfig;
	private File repositoryLocation;

	public MavenRemoteResolver(IResolverConfig resolverConfig, File repositoryLocation) {

		this.resolverConfig = resolverConfig;
		this.repositoryLocation = repositoryLocation;
	}

	@Override
	public IRetrievalResult serveRequest(IRetrievalRequest request) throws MalmoeException {

		try {
			/**
			 * Might want to resolve model too
			 */

			if(request.getType().equals(EExecutionType.MODEL)) {
				
				Naether naether = new NaetherImpl();
				naether.addRemoteRepository(resolverConfig.getResolverName(), "default", resolverConfig.getUrl().toString());

				naether.addDependency(toNotation(request.getIdentifier()), "compile");
				naether.resolveDependencies(false);
				
				produceModel(naether.getDependenciesGraph());

			} else if (request.getType().equals(EExecutionType.MAIN)) {

				Naether naether = new NaetherImpl();
				naether.addRemoteRepository(resolverConfig.getResolverName(), "default", resolverConfig.getUrl().toString());

				naether.addDependency(toNotation(request.getIdentifier()), "compile");
				naether.resolveDependencies(false);
				
			}
		} catch (Exception ex) {
			throw new MalmoeException("Exception while resolving " + request.getIdentifier().toString(), ex);
		}
		return new NotFoundRetrievalResult();
	}

	/**
	 * @param set 
	 * @TODO Need to build models too 
	 * @param dependenciesGraph
	 */
	private void produceModel(Map<String, Map> dependenciesGraph) {
			
		
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
