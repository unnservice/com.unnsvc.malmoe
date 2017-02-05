
package com.unnsvc.malmoe.malmoeResolver;

import java.io.File;

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

public class MalmoeRemoteResolver implements IRemoteResolver {

	private IResolverConfig resolverConfig;
	private File resolverLocation;

	public MalmoeRemoteResolver(IResolverConfig resolverConfig, File resolverLocation) {

		this.resolverConfig = resolverConfig;
		this.resolverLocation = resolverLocation;
	}

	@Override
	public IRetrievalResult serveRequest(IResolvedArtifactRequest request) throws MalmoeException {

		File groupLocation = new File(resolverLocation, request.getIdentifier().getComponentName().toString().replace(".", File.separator));
		File moduleNameLocation = new File(groupLocation, request.getIdentifier().getModuleName().toString());
		File moduleLocation = new File(moduleNameLocation, request.getIdentifier().getVersion().toString());
		if (!moduleLocation.isDirectory()) {
			moduleLocation.mkdirs();
		}

		try {

			if (request instanceof ModelRepositoryResolvedRequest) {

				File modelFile = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
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

}
