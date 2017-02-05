
package com.unnsvc.malmoe.repository;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.unnsvc.malmoe.common.IAccess;
import com.unnsvc.malmoe.common.IAccessManager;
import com.unnsvc.malmoe.common.IIdentityManager;
import com.unnsvc.malmoe.common.IMalmoeRepository;
import com.unnsvc.malmoe.common.IResolvedArtifactRequest;
import com.unnsvc.malmoe.common.IResolvedRequest;
import com.unnsvc.malmoe.common.IRetrievalResult;
import com.unnsvc.malmoe.common.exceptions.MalmoeException;
import com.unnsvc.malmoe.common.requests.ArtifactRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.requests.GenericRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.requests.ModelRepositoryResolvedRequest;
import com.unnsvc.malmoe.common.resolver.IRemoteResolver;
import com.unnsvc.malmoe.common.retrieval.ArtifactRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.FileRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.ModelRetrievalResult;
import com.unnsvc.malmoe.common.retrieval.NotFoundRetrievalResult;
import com.unnsvc.malmoe.repository.config.VirtualRepositoryConfig;
import com.unnsvc.rhena.common.RhenaConstants;

/**
 * Location
 * 
 * @author noname
 *
 */
public class VirtualRepository implements IMalmoeRepository {

	private Logger log = LoggerFactory.getLogger(getClass());
	private File resolverLocation;
	private IAccessManager accessManager;
	private IRemoteResolver resolver;

	public VirtualRepository(VirtualRepositoryConfig virtualConfig, File resolverLocation, IIdentityManager identityManager, IRemoteResolver resolver) {

		this.resolverLocation = resolverLocation;
		this.accessManager = new AccessManager(virtualConfig.getAccessConfig(), identityManager);
		this.resolver = resolver;
	}

	@Override
	public IRetrievalResult serveRequest(IResolvedRequest request) throws MalmoeException {

		if (request instanceof GenericRepositoryResolvedRequest) {

			return serveListing((GenericRepositoryResolvedRequest) request);
		} else {

			return serveArtifact((IResolvedArtifactRequest) request);
		}
	}

	private IRetrievalResult serveArtifact(IResolvedArtifactRequest request) throws MalmoeException {

		return accessManager.withPermissions(request, new IAccess<IRetrievalResult>() {

			public IRetrievalResult execute() throws MalmoeException {

				IRetrievalResult result = resolveLocal(request);
				
				if (result instanceof NotFoundRetrievalResult) {
					if (resolver != null) {
						return resolveRemote(request);
					}
				}
				
				return result;
			}

		}, IMalmoeRepository.ACCESS_REPOSITORY_READ);
	}

	private IRetrievalResult serveListing(GenericRepositoryResolvedRequest request) throws MalmoeException {

		return accessManager.withPermissions(request, new IAccess<IRetrievalResult>() {

			@Override
			public IRetrievalResult execute() throws MalmoeException {

				File location = new File(resolverLocation, request.getRepoRelativePath().replace(".", File.separator));
				if (location.exists() && location.isDirectory()) {

					return new FileRetrievalResult(location);
				}

				return new NotFoundRetrievalResult(request);
			}
		}, IMalmoeRepository.ACCESS_REPOSITORY_READ, IMalmoeRepository.ACCESS_REPOSITORY_LIST);
	}

	protected IRetrievalResult resolveRemote(IResolvedArtifactRequest request) throws MalmoeException {

		return resolver.serveRequest(request);
	}

	protected IRetrievalResult resolveLocal(IResolvedArtifactRequest request) throws MalmoeException {

		File groupLocation = new File(resolverLocation, request.getIdentifier().getComponentName().toString().replace(".", File.separator));
		File moduleNameLocation = new File(groupLocation, request.getIdentifier().getModuleName().toString());
		File moduleLocation = new File(moduleNameLocation, request.getIdentifier().getVersion().toString());

		if (!moduleLocation.isDirectory()) {
			return new NotFoundRetrievalResult(request);
		}

		try {

			if (request instanceof ModelRepositoryResolvedRequest) {

				File modelFile = new File(moduleLocation, RhenaConstants.MODULE_DESCRIPTOR_FILENAME);
				if (modelFile.exists()) {
					return new ModelRetrievalResult(modelFile);
				}
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
